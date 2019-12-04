package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

import static android.view.View.*;
import static android.view.View.VISIBLE;

public class GameScreenActivity extends AppCompatActivity implements View.OnTouchListener {

    private RelativeLayout relativeLayout;
    private int numOfLanes = 3, rand, newRand, score;
    private Handler handler = new Handler();
    private ObjectAnimator animate1Y, animate2Y, animate3Y;
    private Button leftBtn, rightBtn;
    private boolean moveLeft, moveRight, gameOver = false, ifPlaying = true;
    private View carView, life1, life2, life3;
    private TextView scoreView, timerView;
    private Vibrator vibrator;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);


        relativeLayout = findViewById(R.id.gameLayout);
        leftBtn = findViewById(R.id.leftBtn);
        rightBtn = findViewById(R.id.rightBtn);
        life1 = findViewById(R.id.life1);
        life2 = findViewById(R.id.life2);
        life3 = findViewById(R.id.life3);
        scoreView = findViewById(R.id.score);

        leftBtn.setOnTouchListener(this);
        rightBtn.setOnTouchListener(this);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        relativeLayout.post(new Runnable() {
            @Override
            public void run() {
                // Init the car view to the screen.
                carView = new View(GameScreenActivity.this);
                carView.setLayoutParams(new RelativeLayout.LayoutParams(relativeLayout.getWidth() / numOfLanes, 250));
                carView.setBackgroundResource(R.drawable.car);
                carView.setX(relativeLayout.getWidth() / numOfLanes);
                carView.setY(relativeLayout.getHeight() - 250);
                relativeLayout.addView(carView);

                // Init a temporary text view for the timer.
                timerView = new TextView((GameScreenActivity.this));
                timerView.setLayoutParams(new RelativeLayout.LayoutParams(relativeLayout.getWidth() / numOfLanes, 300));
                timerView.setTextSize(80);
                timerView.setTextColor(Color.WHITE);
                timerView.setGravity(Gravity.CENTER);
                timerView.setX(relativeLayout.getWidth() / numOfLanes);
                timerView.setY(relativeLayout.getHeight() / 2 - 300);
                relativeLayout.addView(timerView);
            }
        });

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            new CountDownTimer(4000, 1000) {
                                public void onTick(long sec) {
                                    timerView.setText("" + sec / 1000);
                                }

                                @Override
                                public void onFinish() {
                                    ((ViewGroup) timerView.getParent()).removeView(timerView);
                                }
                            }.start();
                        }
                    });
                    Thread.sleep(3500);

                    rand = new Random().nextInt(numOfLanes);
                    while (!gameOver) {
                        newRand = generateRand(rand);
                        gameLoop();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (ifPlaying) {
                                    score = Integer.parseInt(scoreView.getText().toString());
                                    score += 10;
                                    scoreView.setText("" + score);
                                }
                            }
                        }, 2000);
                        rand = newRand;
                        try {
                            Thread.sleep(1000);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startGameOverActivity();
                    finish();
                }
            }
        }).start();

    }

    // Generate different random number from the last one.
    public int generateRand(int lastRand) {
        int randNum;
        if (lastRand == 0) {
            randNum = new Random().nextInt(2);
            randNum++;
        } else if (lastRand == 2) {
            randNum = new Random().nextInt(2);
        } else {
            randNum = new Random().nextInt(2);
            if (randNum == 1) {
                randNum++;
            }
        }
        return randNum;
    }

    // Key touch events.
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            switch (view.getId()) {
                case R.id.leftBtn:
                    moveLeft = true;
                    break;
                case R.id.rightBtn:
                    moveRight = true;
                    break;
            }
        } else {
            moveLeft = false;
            moveRight = false;
        }
        view.post(new Runnable() {
            @Override
            public void run() {
                changePos();
            }
        });

        return false;
    }

    // Change position of the user.
    public void changePos() {
        float carViewX = carView.getX();
        if (moveLeft) {
            carViewX -= (relativeLayout.getWidth() / 3);

        } else if (moveRight) {
            carViewX += (relativeLayout.getWidth() / 3);
        }

        if (carViewX < 0) {
            carViewX = 0;
        }
        if (carViewX > relativeLayout.getWidth() - carView.getWidth()) {
            carViewX = relativeLayout.getWidth() - carView.getWidth();
        }
        carView.setX(carViewX);

    }

    // Collision detection.
    public boolean collisionDetection(View view) {
        int viewX = (int) view.getX();
        int viewY = (int) view.getY();
        int viewRightTop = view.getWidth() + viewX;
        int viewRightBottom = view.getHeight() + viewY;
        int carViewX = (int) carView.getX();
        int carViewY = (int) carView.getY();

        if (carViewX >= viewX && carViewX < viewRightTop && carViewY >= viewY && carViewY <= viewRightBottom && ifPlaying) {
            if (life3.getVisibility() == VISIBLE) {
                life3.setVisibility(INVISIBLE);
                vibrator.vibrate(300);
                return true;
            } else {
                if (life2.getVisibility() == VISIBLE) {
                    life2.setVisibility(INVISIBLE);
                    vibrator.vibrate(300);
                    return true;
                } else {
                    life1.setVisibility(INVISIBLE);
                    vibrator.vibrate(500);
                    gameOver = true;
                    return true;
                }
            }
        }
        return false;
    }

    // Game loop.
    public void gameLoop() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (newRand == 0) {
                    final View leftView = new View(GameScreenActivity.this);
                    leftView.setLayoutParams(new RelativeLayout.LayoutParams(relativeLayout.getWidth() / numOfLanes, 250));
                    leftView.setBackgroundResource(R.drawable.police_png);
                    relativeLayout.addView(leftView);
                    leftView.setX(newRand * (relativeLayout.getWidth() / numOfLanes));
                    animate1Y = ObjectAnimator.ofFloat(leftView, "translationY", 0f, relativeLayout.getHeight() + 250);
                    animate1Y.setDuration(3000);

                    leftView.post(new Runnable() {
                        @Override
                        public void run() {
                            animate1Y.start();
                            animate1Y.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                    if (collisionDetection(leftView)) {
                                        animate1Y.cancel();
                                    }
                                    if(gameOver){
                                        animate1Y.pause();
                                    }
                                }
                            });

                            animate1Y.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    ((ViewGroup) leftView.getParent()).removeView(leftView);
                                }
                            });

                        }
                    });
                } else if (newRand == 1) {
                    final View centerView = new View(GameScreenActivity.this);
                    centerView.setLayoutParams(new RelativeLayout.LayoutParams(relativeLayout.getWidth() / numOfLanes, 250));
                    centerView.setBackgroundResource(R.drawable.police_png);
                    relativeLayout.addView(centerView);
                    centerView.setX(newRand * (relativeLayout.getWidth() / numOfLanes));
                    animate2Y = ObjectAnimator.ofFloat(centerView, "translationY", 0f, relativeLayout.getHeight() + 250);
                    animate2Y.setDuration(3000);
                    centerView.post(new Runnable() {
                        @Override
                        public void run() {
                            animate2Y.start();
                            animate2Y.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                    if (collisionDetection(centerView)) {
                                        animate2Y.cancel();
                                    }
                                    if(gameOver){
                                        animate2Y.pause();
                                    }
                                }
                            });
                            animate2Y.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    ((ViewGroup) centerView.getParent()).removeView(centerView);
                                }
                            });
                        }
                    });
                } else {
                    final View rightView = new View(GameScreenActivity.this);
                    rightView.setLayoutParams(new RelativeLayout.LayoutParams(relativeLayout.getWidth() / numOfLanes, 250));
                    rightView.setBackgroundResource(R.drawable.police_png);
                    relativeLayout.addView(rightView);
                    rightView.setX(newRand * (relativeLayout.getWidth() / numOfLanes));
                    animate3Y = ObjectAnimator.ofFloat(rightView, "translationY", 0f, relativeLayout.getHeight() + 250);
                    animate3Y.setDuration(3000);
                    rightView.post(new Runnable() {
                        @Override
                        public void run() {
                            animate3Y.start();
                            animate3Y.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                    if (collisionDetection(rightView)) {
                                        animate3Y.cancel();
                                    }
                                    if(gameOver){
                                        animate3Y.pause();
                                    }
                                }
                            });
                            animate3Y.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    ((ViewGroup) rightView.getParent()).removeView(rightView);
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    // Method that start the game over activity when the game ends.
    public void startGameOverActivity() {
        Intent intent = new Intent(this, GameOverActivity.class);
        intent.putExtra("score", score);
        startActivity(intent);
        finish();
    }

    // Method that start the main activity.
    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // Phone back key event.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            startMainActivity();
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onResume() {
        ifPlaying = true;
        super.onResume();
    }

    @Override
    public void onStop() {
        ifPlaying = false;
        super.onStop();
    }

    @Override
    public void onPause() {
        ifPlaying = false;
        super.onPause();
    }
}





