package com.example.gametaixiu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private TextView txtThongBao;
    private ImageView imgLeftDice, imgCenterDice, imgRightDice;
    private Button btnStart, btnRestart;
    private ImageButton btnMusic;
    private RadioGroup rgTaiXiu;
    private RadioButton rbTai, rbXiu;
    private ArrayList<String> dice_faces;
    private Random random = new Random();
    private int a, b, c, sum, res, percent, stopPosition;
    private MediaPlayer music, sound;
    private boolean isMusicPlayed = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        getImagesName();
        initMusic();

//        playMusic(R.raw.music, 0);
        music.start();

        btnMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMusicPlayed) {
                    isMusicPlayed = false;

                    btnMusic.setImageResource(R.drawable.ic_music_off);

                    music.stop();
                }else {
                    isMusicPlayed = true;

                    btnMusic.setImageResource(R.drawable.ic_music_on);

//                    playMusic(R.raw.music, 0);
                    music.start();
                }
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!check())    {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Vui lòng chọn (Tài) hoặc (Xỉu) để bắt đầu!")
                            .setPositiveButton("OK", null)
                            .show();
                }else   {
                    sound.start();
//                    if (music.isPlaying()) {
//                        stopPosition = music.getCurrentPosition();
//
//                        music.stop();
//
//                    }
//
//                    playMusic(R.raw.play_sound, stopPosition);

                    lacXiNgau();
                }
            }

        });

        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableViews();
                restart();
            }
        });
    }

    private void lacXiNgau() {
        disableViews();
        new CountDownTimer(3000, 300) {
            @Override
            public void onTick(long millisUntilFinished) {
                a = random.nextInt(6);
                b = random.nextInt(6);
                c = random.nextInt(6);

                ganHinh(a, imgLeftDice);
                ganHinh(b, imgCenterDice);
                ganHinh(c, imgRightDice);
            }

            @Override
            public void onFinish() {
//                music.stop();
//
//                if (isMusicPlayed) {
//                    playMusic(R.raw.music, stopPosition);
//                }

                percent = random.nextInt(100) + 1;

                res = rgTaiXiu.getCheckedRadioButtonId() == R.id.rbXiu ? 0 : 1;
                Log.d("RES", String.valueOf(percent));
                Log.d("RES", String.valueOf(res));

                if (percent > 30) {
                    if (res == 1) {
                        Log.d("RES", "TAI -> XIU");
                        while (a + b + c + 3 >= 11) {
                            a = random.nextInt(6);
                            b = random.nextInt(6);
                            c = random.nextInt(6);
                        }
                    }else {
                        Log.d("RES", "XIU -> TAI");
                        while (a + b + c + 3 <= 10) {
                            a = random.nextInt(6);
                            b = random.nextInt(6);
                            c = random.nextInt(6);
                        }
                    }
                }else {
                    if (res == 1) {
                        Log.d("RES", "TAI");
                        while (a + b + c + 3 <= 10) {
                            a = random.nextInt(6);
                            b = random.nextInt(6);
                            c = random.nextInt(6);
                        }
                    }else {
                        Log.d("RES", "XIU");
                        while (a + b + c + 3 >= 11) {
                            a = random.nextInt(6);
                            b = random.nextInt(6);
                            c = random.nextInt(6);
                        }
                    }
                }

                ganHinh(a, imgLeftDice);
                ganHinh(b, imgCenterDice);
                ganHinh(c, imgRightDice);

                sum = 0;
                Log.d("RES", a+" "+b+" "+ c);
                sum = a + b + c + 3;
                if (sum <= 10 && rgTaiXiu.getCheckedRadioButtonId() == R.id.rbXiu)  {
                    txtThongBao.setText("Kết quả: Xỉu("+sum+")"+"\nChúc mừng bạn đã đoán chính xác");
                }else if (sum >= 11 && rgTaiXiu.getCheckedRadioButtonId() == R.id.rbTai)    {
                    txtThongBao.setText("Kết quả: Tài("+sum+")"+"\nChúc mừng bạn đã đoán chính xác");
                }else   {
                    if(sum <= 10)   {
                        txtThongBao.setText("Kết quả: Xỉu("+sum+")"+"\nChúc bạn may mắn lần sau");
                    }else   {
                        txtThongBao.setText("Kết quả: Tài("+sum+")"+"\nChúc bạn may mắn lần sau");
                    }

                }
                txtThongBao.setVisibility(View.VISIBLE);
                btnRestart.setEnabled(true);
            }
        }.start();
    }

    private void disableViews() {
        btnStart.setEnabled(false);
        rbTai.setClickable(false);
        rbXiu.setClickable(false);
    }

    private void enableViews()  {
        btnStart.setEnabled(true);
        btnRestart.setEnabled(false);
        rbTai.setClickable(true);
        rbXiu.setClickable(true);
    }

    private void ganHinh(int num, ImageView img)  {
        int idHinh = getResources().getIdentifier(dice_faces.get(num), "drawable", getPackageName());
        img.setImageResource(idHinh);
    }

    private void restart() {
        rgTaiXiu.clearCheck();
        txtThongBao.setVisibility(View.GONE);
    }

    private boolean check()    {
        if (rgTaiXiu.getCheckedRadioButtonId() == -1) {
            return false;
        }
        return true;
    }

    private void getImagesName() {
        String[] temp = getResources().getStringArray(R.array.dice_faces);
        dice_faces = new ArrayList<>(Arrays.asList(temp));
    }

    private void initViews() {
        imgLeftDice = findViewById(R.id.xxl);
        imgCenterDice = findViewById(R.id.xxc);
        imgRightDice = findViewById(R.id.xxr);

        btnStart = findViewById(R.id.btnStart);
        btnRestart = findViewById(R.id.btnRestart);
        btnMusic = findViewById(R.id.btn_music);

        rgTaiXiu = findViewById(R.id.rgTaiXiu);

        txtThongBao = findViewById(R.id.txtThongBao);

        rbTai = findViewById(R.id.rbTai);
        rbXiu = findViewById(R.id.rbXiu);
    }

    private void initMusic() {
        music = MediaPlayer.create(this, R.raw.music);
        music.setLooping(true);

        sound = MediaPlayer.create(this, R.raw.play_sound);
    }

    private void playMusic(int id, int current) {
        music = MediaPlayer.create(this, id);

        if (current != 0) {
            music.seekTo(current);
        }

        music.setLooping(true);

        music.start();
    }
}