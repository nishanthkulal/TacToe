package ajiet.cse.tactoe;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView[][] buttons = new ImageView[3][3];
    private boolean player1Turn = true;
    private int roundCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Game Start")
                .setMessage("Player 1: X\nPlayer 2: O")
                .setPositiveButton("OK", null)
                .show();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "imageView_" + i + j;
                int resourceID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resourceID);
                buttons[i][j].setOnClickListener(this);
            }
        }

        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showResetConfirmationDialog();
            }
        });
    }

    @Override
    public void onClick(View v) {
        ImageView imageView = (ImageView) v;

        if (player1Turn) {
            imageView.setImageResource(R.drawable.x_image);
        } else {
            imageView.setImageResource(R.drawable.o_image);
        }

        roundCount++;

        if (checkForWin()) {
            if (player1Turn) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showWinnerDialog("Player 1");
                    }
                }, 450); // Delay of 1 second
            } else {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showWinnerDialog("Player 2");
                    }
                }, 450); // Delay of 1 second
            }
        } else if (roundCount == 9) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showDrawDialog();
                }
            }, 450); // Delay of 1 second
        } else {
            player1Turn = !player1Turn;
        }
    }

    private boolean checkForWin() {
        String[][] field = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = getCellText(buttons[i][j]);
            }
        }

        // Check rows
        for (int i = 0; i < 3; i++) {
            if (!field[i][0].isEmpty() && field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2])) {
                return true;
            }
        }

        // Check columns
        for (int i = 0; i < 3; i++) {
            if (!field[0][i].isEmpty() && field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i])) {
                return true;
            }
        }

        // Check diagonals
        if (!field[0][0].isEmpty() && field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2])) {
            return true;
        }

        if (!field[0][2].isEmpty() && field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0])) {
            return true;
        }

        return false;
    }

    private String getCellText(ImageView imageView) {
        if (imageView.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.x_image).getConstantState()) {
            return "X";
        } else if (imageView.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.o_image).getConstantState()) {
            return "O";
        } else {
            return "";
        }
    }

    private void showWinnerDialog(String winner) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Game Over")
                .setMessage(winner + " wins!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        resetGame();
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void showDrawDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Game Over")
                .setMessage("It's a draw!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        resetGame();
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void showResetConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reset Confirmation")
                .setMessage("Are you sure you want to reset the game?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        resetGame();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void resetGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setImageResource(R.drawable.empty_img);
            }
        }

        roundCount = 0;
        player1Turn = true;
    }
}
