package edu.neu.madcourse.kinshukjuneja.utils.horoscope;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.kinshukjuneja.listener.horoscope.HoroscopePeopleLoadedListener;
import edu.neu.madcourse.kinshukjuneja.listener.horoscope.HoroscopeReadyListener;
import edu.neu.madcourse.kinshukjuneja.dao.horoscope.UserDetail;
import edu.neu.madcourse.kinshukjuneja.utils.Table;

public class HoroscopeFirebaseHelper {

    private static HoroscopeFirebaseHelper singletonRef;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    private HoroscopeReadyListener horoscopeReadyListener;
    private HoroscopePeopleLoadedListener horoscopePeopleLoadedListener;

    private ChildEventListener peopleNearMeListener;
    private Long numPeopleNearMe;
    private Long numPeopleNearMeLoaded;

    private boolean isNotificationEnabled;
    private boolean isMusicEnabled;

    private SharedPreferences sharedPreferences;

    private HoroscopeFirebaseHelper() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
    }

    public static HoroscopeFirebaseHelper getSingletonRef() {
        if(singletonRef == null) {
            singletonRef = new HoroscopeFirebaseHelper();
        }
        return singletonRef;
    }

    public void signInAnonymously() {
        mAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    fetchCurrentUserDetail();
                }
            }
        });
    }

    public void fetchCurrentUserDetail() {
        final DatabaseReference userDetailRef = mDatabase.getReference(Table.HUSERDETAIL.getTableName());

        userDetailRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String uid = mAuth.getCurrentUser().getUid();
                for(DataSnapshot userDetailNode : dataSnapshot.getChildren()) {
                    UserDetail ud = userDetailNode.getValue(UserDetail.class);
                    if(ud.getUid().equals(uid)) {
                        cacheUserDetails(ud, userDetailNode.getKey());
                        horoscopeReadyListener.onHoroscopeReady();
                        return;
                    }
                }
                horoscopeReadyListener.onHoroscopeReady();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void createNewUser(String name, String dateOfBirth, String city) {
        final DatabaseReference userDetailRef = mDatabase.getReference(Table.HUSERDETAIL.getTableName());
        String uid = mAuth.getCurrentUser().getUid();

        String key = userDetailRef.push().getKey();
        UserDetail newUser = new UserDetail(uid, name, dateOfBirth, city);
        userDetailRef.child(key).setValue(newUser);
        cacheUserDetails(newUser, key);
    }

    public void cacheUserDetails(UserDetail userDetail, String key) {
        HoroscopeCache.currentUser = userDetail;
        HoroscopeCache.zodiac = ZodiacHelper.getZodiacFromDob(userDetail.getDateOfBirth());
        HoroscopeCache.key = key;
    }

    public void addFriend(String name, String dateOfBirth, String city) {
        final DatabaseReference friendRef = mDatabase.getReference(Table.HFRIEND.getTableName());

        String usernameId = friendRef.push().getKey();
        UserDetail newFriend = new UserDetail(HoroscopeCache.currentUser.getUid(), name, dateOfBirth, city);
        friendRef.child(usernameId).setValue(newFriend);

        Zodiac friendZodiac = ZodiacHelper.getZodiacFromDob(dateOfBirth);
        Friend friend = new Friend(name, city, dateOfBirth, ZodiacHelper.getCompatibilityWith(friendZodiac), friendZodiac, usernameId);
        addFriendToCache(friend);
    }

    public void updateCurrentUser(String name, String dateOfBirth, String city) {
        final DatabaseReference userDetailRef = mDatabase.getReference(Table.HUSERDETAIL.getTableName());
        boolean zodiacUpdateRequired = false;
        boolean cityUpdateRequired = false;

        if(!name.equals(HoroscopeCache.currentUser.getName())) userDetailRef.child(HoroscopeCache.key).child("name").setValue(name);
        if(!dateOfBirth.equals(HoroscopeCache.currentUser.getDateOfBirth())) {
            userDetailRef.child(HoroscopeCache.key).child("dateOfBirth").setValue(dateOfBirth);
            zodiacUpdateRequired = true;
        }
        if(!city.equals(HoroscopeCache.currentUser.getCity())) {
            userDetailRef.child(HoroscopeCache.key).child("city").setValue(city);
            cityUpdateRequired = true;
        }

        HoroscopeCache.currentUser = new UserDetail(HoroscopeCache.currentUser.getUid(), name, dateOfBirth, city);

        if(zodiacUpdateRequired) {
            HoroscopeCache.zodiac = ZodiacHelper.getZodiacFromDob(HoroscopeCache.currentUser.getDateOfBirth());
            ZodiacHelper.updateCompatibiltyWithFriends();
        }
        if(cityUpdateRequired) {
            resetPeopleNearMeListener();
        }
    }

    public void updateFriend(int friendIndex, String name, String dateOfBirth, String city) {
        final DatabaseReference friendRef = mDatabase.getReference(Table.HFRIEND.getTableName());
        Friend friend = HoroscopeCache.friends.get(friendIndex);

        if(!name.equals(friend.getName())) {
            friendRef.child(friend.getKey()).child("name").setValue(name);
            friend.setName(name);
        }
        if(!dateOfBirth.equals(friend.getDob())) {
            HoroscopeCache.friendsByZodiacMap.get(friend.getZodiac()).remove(friend);

            friendRef.child(friend.getKey()).child("dateOfBirth").setValue(dateOfBirth);
            friend.setDob(dateOfBirth);
            friend.setZodiac(ZodiacHelper.getZodiacFromDob(dateOfBirth));
            friend.setCompatibility(ZodiacHelper.getCompatibilityWith(friend.getZodiac()));

            addFriendToZodiacMap(friend);
        }
        if(!city.equals(friend.getCity())) {
            friendRef.child(friend.getKey()).child("city").setValue(city);
            friend.setCity(city);
        }
    }

    public void deleteFriend(int friendIndex) {
        final DatabaseReference friendRef = mDatabase.getReference(Table.HFRIEND.getTableName());
        Friend friend = HoroscopeCache.friends.get(friendIndex);

        friendRef.child(friend.getKey()).setValue(null);
        HoroscopeCache.friends.remove(friendIndex);
        HoroscopeCache.friendsByZodiacMap.get(friend.getZodiac()).remove(friend);
    }

    public void getFriends() {
        final DatabaseReference friendRef = mDatabase.getReference(Table.HFRIEND.getTableName());

        String uid = HoroscopeCache.currentUser.getUid();
        friendRef.orderByChild("uid").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot userDetailNode : dataSnapshot.getChildren()) {
                    UserDetail ud = userDetailNode.getValue(UserDetail.class);
                    Zodiac friendZodiac = ZodiacHelper.getZodiacFromDob(ud.getDateOfBirth());
                    Friend friend = new Friend(ud.getName(), ud.getCity(), ud.getDateOfBirth(), ZodiacHelper.getCompatibilityWith(friendZodiac), friendZodiac, userDetailNode.getKey());
                    addFriendToCache(friend);
                }
                horoscopePeopleLoadedListener.onFriendsLoaded();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void getPeopleNearMe() {
        final DatabaseReference userDetailRef = mDatabase.getReference(Table.HUSERDETAIL.getTableName());

        userDetailRef.orderByChild("city").equalTo(HoroscopeCache.currentUser.getCity()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                numPeopleNearMe = dataSnapshot.getChildrenCount() - 1;
                numPeopleNearMeLoaded = 0l;
                attachPeopleNearMeListener(userDetailRef);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void attachPeopleNearMeListener(DatabaseReference userDetailRef) {
        if(numPeopleNearMe == 0) horoscopePeopleLoadedListener.onPeopleNearMeLoaded();
        peopleNearMeListener = userDetailRef.orderByChild("city").equalTo(HoroscopeCache.currentUser.getCity()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                UserDetail person = dataSnapshot.getValue(UserDetail.class);
                if(!person.getUid().equals(HoroscopeCache.currentUser.getUid())) {
                    Zodiac personZodiac = ZodiacHelper.getZodiacFromDob(person.getDateOfBirth());
                    Friend newPerson = new Friend(person.getName(), person.getCity(), person.getDateOfBirth(), ZodiacHelper.getCompatibilityWith(personZodiac), personZodiac, dataSnapshot.getKey());
                    HoroscopeCache.nearMe.add(newPerson);
                    ++numPeopleNearMeLoaded;

                    if(numPeopleNearMe == numPeopleNearMeLoaded) {
                        horoscopePeopleLoadedListener.onPeopleNearMeLoaded();
                    } else if(numPeopleNearMeLoaded > numPeopleNearMe) {
                        if(isNotificationEnabled) HoroscopeCache.notificationHelper.generateDrawerNotification(newPerson.getName(), newPerson.getCompatibility());
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public boolean isUserRegistered() {
        return HoroscopeCache.currentUser != null;
    }

    public void attachHoroscopeReadyListener(HoroscopeReadyListener horoscopeReadyListener) {
        this.horoscopeReadyListener = horoscopeReadyListener;
    }

    public void attachHoroscopePeopleLoadedListener(HoroscopePeopleLoadedListener horoscopePeopleLoadedListener) {
        this.horoscopePeopleLoadedListener = horoscopePeopleLoadedListener;
    }

    public void resetPeopleNearMeListener() {
        removePeopleNearMeListener();
        getPeopleNearMe();
    }

    public void removePeopleNearMeListener() {
        if(peopleNearMeListener != null) {
            final DatabaseReference userDetailRef = mDatabase.getReference(Table.HUSERDETAIL.getTableName());
            userDetailRef.removeEventListener(peopleNearMeListener);
            HoroscopeCache.nearMe = new ArrayList<>();
        }
    }

    public void setNotificationEnabled(boolean isNotificationEnabled) {
        this.isNotificationEnabled = isNotificationEnabled;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("notificationOn", isNotificationEnabled);
        editor.commit();
    }

    public void setMusicEnabled(boolean isMusicEnabled) {
        this.isMusicEnabled = isMusicEnabled;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("musicOn", isMusicEnabled);
        editor.commit();
    }

    public boolean isNotificationEnabled() {
        return isNotificationEnabled;
    }

    public boolean isMusicEnabled() {
        return isMusicEnabled;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void loadFromSharedPreferences() {
        isNotificationEnabled = sharedPreferences.getBoolean("notificationOn", true);
        isMusicEnabled = sharedPreferences.getBoolean("musicOn", true);
    }

    public void addFriendToCache(Friend friend) {
        HoroscopeCache.friends.add(friend);
        addFriendToZodiacMap(friend);
    }

    public void addFriendToZodiacMap(Friend friend) {
        if(HoroscopeCache.friendsByZodiacMap.containsKey(friend.getZodiac())) {
            HoroscopeCache.friendsByZodiacMap.get(friend.getZodiac()).add(friend);
        } else {
            List<Friend> friends = new ArrayList<>();
            friends.add(friend);
            HoroscopeCache.friendsByZodiacMap.put(friend.getZodiac(), friends);
        }
    }

}
