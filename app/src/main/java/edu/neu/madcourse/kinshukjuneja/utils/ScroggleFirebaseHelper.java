package edu.neu.madcourse.kinshukjuneja.utils;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.neu.madcourse.kinshukjuneja.R;
import edu.neu.madcourse.kinshukjuneja.dao.Score;
import edu.neu.madcourse.kinshukjuneja.dao.UserName;
import edu.neu.madcourse.kinshukjuneja.recyclerview.ScroggleLeaderboardAdapter;
import edu.neu.madcourse.kinshukjuneja.recyclerview.ScroggleScoreboardAdapter;

public class ScroggleFirebaseHelper {

    private static ScroggleFirebaseHelper singletonRef;

    private static final int MAX_LEADERBOARD_ITEMS = 10;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    private List<Score> leaderboardTotalScores;
    private List<Score> leaderboardHighestScores;
    private Map<String, String> usernames;

    private List<Score> scoreboardTotalScores;
    private List<Score> scoreboardHighestScores;

    private String currentUsername;

    private boolean usernamesReady;
    private boolean leaderboardTotalScoresReady;
    private boolean leaderboardHighestScoresReady;
    private boolean signInReady;

    private static final String DEFAULT_USERNAME_TEMPLATE = "Guest%d";

    private ScroggleFirebaseHelper() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        leaderboardTotalScores = new ArrayList<>();
        leaderboardHighestScores = new ArrayList<>();
        scoreboardTotalScores = new ArrayList<>();
        scoreboardHighestScores = new ArrayList<>();
        usernames = new HashMap<>();
    }

    public static ScroggleFirebaseHelper getSingletonRef() {
        if(singletonRef == null) {
            singletonRef = new ScroggleFirebaseHelper();
        }
        return singletonRef;
    }

    public void signInAnonymously() {
        mAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    fetchCurrentUsername();
                }
            }
        });
    }

    public void fetchCurrentUsername() {
        final DatabaseReference usernameRef = mDatabase.getReference(Table.USERNAME.getTableName());

        usernameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long numChildren = dataSnapshot.getChildrenCount();
                String uid = mAuth.getCurrentUser().getUid();
                for(DataSnapshot usernameNode : dataSnapshot.getChildren()) {
                    UserName un = usernameNode.getValue(UserName.class);
                    if(un.getUid().equals(uid)) {
                        currentUsername = un.getUsername();
                        signInReady = true;
                        return;
                    }
                }
                String usernameId = usernameRef.push().getKey();
                UserName newUserName = new UserName(uid, String.format(DEFAULT_USERNAME_TEMPLATE, numChildren + 1));
                usernameRef.child(usernameId).setValue(newUserName);
                currentUsername = newUserName.getUsername();
                signInReady = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void addNewScore(int totalScore, int highestScore, String highestScoredWord) {
        DatabaseReference scoreRef = mDatabase.getReference(Table.SCORE.getTableName());
        String scoreId = scoreRef.push().getKey();
        Score newScore = new Score(mAuth.getCurrentUser().getUid(), totalScore, highestScore, highestScoredWord, new Date().getTime());
        scoreRef.child(scoreId).setValue(newScore);
    }

    public void populateScoreboard(final boolean orderedByHighestWord, ScroggleScoreboardAdapter scroggleScoreboardAdapter, final Activity activity) {
        scroggleScoreboardAdapter.setScores(orderedByHighestWord ? scoreboardHighestScores : scoreboardTotalScores);

        RecyclerView recyclerView = activity.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(scroggleScoreboardAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
    }

    public void buildScoreboard() {
        while(!signInReady);
        DatabaseReference scoreRef = mDatabase.getReference(Table.SCORE.getTableName());
        scoreRef.orderByChild("uid").equalTo(mAuth.getCurrentUser().getUid())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Score score = dataSnapshot.getValue(Score.class);
                        scoreboardTotalScores.add(score);
                        Collections.sort(scoreboardTotalScores, new Comparator<Score>() {
                            @Override
                            public int compare(Score score, Score t1) {
                                return t1.getTotalScore() - score.getTotalScore();
                            }
                        });
                        scoreboardHighestScores.add(score);
                        Collections.sort(scoreboardHighestScores, new Comparator<Score>() {
                            @Override
                            public int compare(Score score, Score t1) {
                                return t1.getHighestScore() - score.getHighestScore();
                            }
                        });
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
    }

    public void populateLeaderboard(final boolean orderedByHighestWord, ScroggleLeaderboardAdapter scroggleLeaderboardAdapter, final Activity activity) {
        while(!signInReady && !usernamesReady && !leaderboardTotalScoresReady && !leaderboardHighestScoresReady);
        scroggleLeaderboardAdapter.setUsernames(usernames);
        scroggleLeaderboardAdapter.setScores(orderedByHighestWord ? leaderboardHighestScores : leaderboardTotalScores);

        RecyclerView recyclerView = activity.findViewById(R.id.lbrecyclerView);
        recyclerView.setAdapter(scroggleLeaderboardAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
    }

    public void buildInitialLeaderboard(Activity activity) {
        fetchUsernames();
        fetchLeaderboardTotalScores();
        fetchLeaderboardHighestScores();
        while(!usernamesReady && !leaderboardTotalScoresReady && !leaderboardHighestScoresReady);
        attachUsernameListener();
        attachNewScoreListener(activity);
    }

    private void fetchUsernames() {
        final DatabaseReference usernameRef = mDatabase.getReference(Table.USERNAME.getTableName());
        usernameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot usernameNode : dataSnapshot.getChildren()) {
                    UserName un = usernameNode.getValue(UserName.class);
                    usernames.put(un.getUid(), un.getUsername());
                }
                usernamesReady = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private void fetchLeaderboardTotalScores() {
        DatabaseReference scoreRef = mDatabase.getReference(Table.SCORE.getTableName());

        scoreRef.orderByChild("totalScore").limitToLast(MAX_LEADERBOARD_ITEMS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot scoreNode : dataSnapshot.getChildren()) {
                    Score score = scoreNode.getValue(Score.class);
                    leaderboardTotalScores.add(score);
                    Collections.sort(leaderboardTotalScores, new Comparator<Score>() {
                        @Override
                        public int compare(Score score, Score t1) {
                            return t1.getTotalScore() - score.getTotalScore();
                        }
                    });
                }
                leaderboardTotalScoresReady = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private void fetchLeaderboardHighestScores() {
        DatabaseReference scoreRef = mDatabase.getReference(Table.SCORE.getTableName());

        scoreRef.orderByChild("highestScore").limitToLast(MAX_LEADERBOARD_ITEMS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot scoreNode : dataSnapshot.getChildren()) {
                    Score score = scoreNode.getValue(Score.class);
                    leaderboardHighestScores.add(score);
                    Collections.sort(leaderboardHighestScores, new Comparator<Score>() {
                        @Override
                        public int compare(Score score, Score t1) {
                            return t1.getHighestScore() - score.getHighestScore();
                        }
                    });
                }
                leaderboardHighestScoresReady = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private void attachUsernameListener() {
        DatabaseReference newUsernameRef = mDatabase.getReference(Table.USERNAME.getTableName());
        newUsernameRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                UserName un = dataSnapshot.getValue(UserName.class);
                usernames.put(un.getUid(), un.getUsername());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                UserName un = dataSnapshot.getValue(UserName.class);
                usernames.put(un.getUid(), un.getUsername());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private void attachNewScoreListener(final Activity activity) {
        final NotificationHelper notificationHelper = new NotificationHelper(activity);

        DatabaseReference newScoreRef = mDatabase.getReference(Table.SCORE.getTableName());
        newScoreRef.orderByChild("timestamp").startAt(new Date().getTime()).addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Score newScore = dataSnapshot.getValue(Score.class);
                boolean notificationForTotalScoreRequired = false;
                boolean notificationForHighestScoreRequired = false;

                if(leaderboardTotalScores.isEmpty() ||
                        leaderboardTotalScores.size() < MAX_LEADERBOARD_ITEMS ||
                        leaderboardTotalScores.get(leaderboardTotalScores.size() - 1).getTotalScore() < newScore.getTotalScore()) {
                    leaderboardTotalScores.add(newScore);
                    Collections.sort(leaderboardTotalScores, new Comparator<Score>() {
                        @Override
                        public int compare(Score score, Score t1) {
                            return t1.getTotalScore() - score.getTotalScore();
                        }
                    });
                    if(leaderboardTotalScores.size() > MAX_LEADERBOARD_ITEMS) leaderboardTotalScores.remove(leaderboardTotalScores.size() - 1);
                    if(newScore.getUid() != mAuth.getCurrentUser().getUid()) notificationForTotalScoreRequired = true;
                }

                if(leaderboardHighestScores.isEmpty() ||
                        leaderboardHighestScores.size() < MAX_LEADERBOARD_ITEMS ||
                        leaderboardHighestScores.get(leaderboardHighestScores.size() - 1).getHighestScore() < newScore.getHighestScore()) {
                    leaderboardHighestScores.add(newScore);
                    Collections.sort(leaderboardHighestScores, new Comparator<Score>() {
                        @Override
                        public int compare(Score score, Score t1) {
                            return t1.getHighestScore() - score.getHighestScore();
                        }
                    });
                    if(leaderboardHighestScores.size() > MAX_LEADERBOARD_ITEMS) leaderboardHighestScores.remove(leaderboardHighestScores.size() - 1);
                    if(newScore.getUid() != mAuth.getCurrentUser().getUid()) notificationForHighestScoreRequired = true;
                }

                if(notificationForTotalScoreRequired) {
                    notificationHelper.generateDrawerNotification(usernames.get(newScore.getUid()), newScore.getTotalScore(), true);
                } else if(notificationForHighestScoreRequired) {
                    notificationHelper.generateDrawerNotification(usernames.get(newScore.getUid()), newScore.getHighestScore(), false);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    public void updateUsername(final String newUsername, final Activity activity) {
        final DatabaseReference usernameRef = mDatabase.getReference(Table.USERNAME.getTableName());

        usernameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String uid = mAuth.getCurrentUser().getUid();
                String key = null;
                for(DataSnapshot usernameNode : dataSnapshot.getChildren()) {
                    UserName un = usernameNode.getValue(UserName.class);
                    if(un.getUsername().equals(newUsername)) {
                        Toast.makeText(activity, "Username already taken", Toast.LENGTH_LONG).show();
                        return;
                    } else if(un.getUid().equals(uid)) {
                        key = usernameNode.getKey();
                    }
                }
                usernameRef.child(key).child("username").setValue(newUsername);
                Toast.makeText(activity, "Username Changed Successfully", Toast.LENGTH_SHORT).show();
                currentUsername = newUsername;
                activity.finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(activity, "Error changing username", Toast.LENGTH_LONG).show();
            }
        });
    }

    public String getCurrentUsername() {
        while(!signInReady);
        return currentUsername;
    }

}
