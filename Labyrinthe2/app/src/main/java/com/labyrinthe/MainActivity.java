package com.labyrinthe;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;

public class MainActivity extends Activity {
    // Identifiant de la boÓte de dialogue de victoire
    public static final int VICTORY_DIALOG = 0;
    // Identifiant de la boÓte de dialogue de dÈfaite
    public static final int DEFEAT_DIALOG = 1;
    private  MediaPlayer mediaPlayer;
    // Le moteur graphique du jeu
    private LabyrintheView mView = null;
    // Le moteur physique du jeu
    private LabyrintheEngine mEngine = null;
    public Boolean win;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mView = new LabyrintheView(this);
        setContentView(mView);

        mEngine = new LabyrintheEngine(this);

        Boule b = new Boule();
        mView.setBoule(b);
        mEngine.setBoule(b);

        List<Bloc> mList = mEngine.buildLabyrinthe();
        mView.setBlocks(mList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mEngine.resume();
    }

    @Override
    protected void onPause() {
        super.onStop();
        mEngine.stop();
    }

    @Override
    public Dialog onCreateDialog (int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch(id) {
            case VICTORY_DIALOG:
                win = true;
                builder.setCancelable(false)
                        .setMessage("Bravo, vous avez gagnÈ !")
                        .setTitle("Champion ! Le roi des Zˆrglubienotchs est mort gr‚ce ‡ vous !")
                        .setNeutralButton("Recommencer", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // L'utilisateur peut recommencer s'il le veut
                                mEngine.reset();
                                mEngine.resume();
                            }
                        });
                break;
            case DEFEAT_DIALOG:
            win = false;
                builder.setCancelable(false)
                        .setMessage("La Terre a èté dètruite à cause de vos erreurs.")
                        .setTitle("Bah bravo !")
                        .setNeutralButton("Recommencer", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mEngine.reset();
                                mEngine.resume();
                            }
                        });
        }
        return builder.create();
    }

    @Override
    public void onPrepareDialog (int id, Dialog box) {
        // A chaque fois qu'une boÓte de dialogue est lancÈe, on arrÍte le moteur physique
        Integer soundplay;


        if(win == true) {
          soundplay =  R.raw.victory;
        }else {
            soundplay = R.raw.fatality;
        }

        this.mediaPlayer = MediaPlayer.create(getApplicationContext(),soundplay);
        mEngine.stop();

        mediaPlayer.setOnCompletionListener(new  MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {

                mediaPlayer.release();
            }
        });
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.start();
        }catch (Exception e){ e.printStackTrace();
        }

    }
}