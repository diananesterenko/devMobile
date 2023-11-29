package com.example.chohan

import android.app.ActionBar.LayoutParams
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import android.widget.Toast

const val STARTING_SCORE = 100
const val STARTING_BET = 10

class MainActivity : AppCompatActivity() {

    private lateinit var controllerButtonOdd: Button
    private lateinit var controllerButtonEven: Button

    private lateinit var controllerButtonAmount: Button
    private lateinit var controllerButtonPlay: Button

    private lateinit var controllerImageViewDice1: ImageView
    private lateinit var controllerImageViewDice2: ImageView

    private lateinit var controllerTextViewSum: TextView
    private lateinit var controllerTextViewAmount: TextView
    private lateinit var controllerTextViewScore: TextView

    private val model: ChoHanGameModel = ChoHanGameModel(STARTING_SCORE, STARTING_BET)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        controllerButtonOdd = findViewById(R.id.buttonOdd)
        controllerButtonEven = findViewById(R.id.buttonEven)
        controllerButtonAmount = findViewById(R.id.buttonAmount)
        controllerButtonPlay = findViewById(R.id.buttonPlay)

        controllerImageViewDice1 = findViewById(R.id.imageViewDice1)
        controllerImageViewDice2 = findViewById(R.id.imageViewDice2)

        controllerTextViewSum = findViewById(R.id.textViewSum)
        controllerTextViewAmount = findViewById(R.id.textViewBet)
        controllerTextViewScore = findViewById(R.id.textViewScore)

        controllerButtonOdd.visibility = View.INVISIBLE
        controllerButtonEven.visibility = View.INVISIBLE

        controllerTextViewSum.text = ""
        controllerTextViewAmount.text = getString(R.string.bet, STARTING_BET.toString())
        controllerTextViewScore.text = getString(R.string.score, STARTING_SCORE.toString())

        controllerButtonOdd.setOnClickListener {
            controllerButtonPlay.visibility = View.VISIBLE
            model.setChoice(Choice.ODD)

            model.throwDice()

            val result = model.getResult()
            val firstDice = model.getFirstDice()
            val secondDice = model.getSecondDice()

            this.changeDice(firstDice, 1)
            this.changeDice(secondDice, 2)

            controllerTextViewSum.text = getString(R.string.numberSum, (firstDice + secondDice).toString())

            if(result == Result.WON) {
                model.receiveWinnings()
            }

            val dialogText = when(result) {
                Result.WON -> "You won!"
                Result.LOST -> "You Lost!"
            }

            val builder = AlertDialog.Builder(this)
                .setTitle("Result")
                .setMessage(dialogText)
                .setCancelable(false)
                .setPositiveButton("OK") { dialog, _ -> dialog.dismiss()
                }
            val dialog: AlertDialog = builder.create()
            dialog.show()

            controllerTextViewScore.text = getString(R.string.score, model.getScore().toString())
            controllerButtonOdd.visibility = View.INVISIBLE
            controllerButtonEven.visibility = View.INVISIBLE
        }

        controllerButtonEven.setOnClickListener {
            controllerButtonPlay.visibility = View.VISIBLE
            model.setChoice(Choice.EVEN)

            model.throwDice()

            val result = model.getResult()
            val firstDice = model.getFirstDice()
            val secondDice = model.getSecondDice()

            this.changeDice(firstDice, 1)
            this.changeDice(secondDice, 2)

            controllerTextViewSum.text = getString(R.string.numberSum, (firstDice + secondDice).toString())

            if(result == Result.WON) {
                model.receiveWinnings()
            }

            val dialogText = when(result){
                Result.WON -> "You won!"
                Result.LOST -> "You Lost!"
            }

            val builder = AlertDialog.Builder(this)
                .setTitle("Result")
                .setMessage(dialogText)
                .setCancelable(false)
                .setPositiveButton("OK") { dialog, _ -> dialog.dismiss()
                }
            val dialog: AlertDialog = builder.create()
            dialog.show()

            controllerTextViewScore.text = getString(R.string.score, model.getScore().toString())
            controllerButtonOdd.visibility = View.INVISIBLE
            controllerButtonEven.visibility = View.INVISIBLE
        }

        controllerButtonPlay.setOnClickListener {
            if(model.getScore() <= 0){
                val builder = AlertDialog.Builder(this)
                    .setTitle("Total loss!")
                    .setMessage("You lost all points. Reseting the game.")
                    .setCancelable(false)
                    .setPositiveButton("OK") { dialog, _ -> dialog.dismiss()
                    }
                val dialog: AlertDialog = builder.create()
                dialog.show()
                model.reset()
            }
            else{
                controllerButtonPlay.visibility = View.INVISIBLE
                controllerButtonOdd.visibility = View.VISIBLE
                controllerButtonEven.visibility = View.VISIBLE
                model.placeBet()
            }
            controllerTextViewScore.text = getString(R.string.score, model.getScore().toString())
            controllerTextViewAmount.text = getString(R.string.bet, model.getBet().toString())
        }

        controllerButtonAmount.setOnClickListener {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.amount_window)

            val buttonOk: Button = dialog.findViewById(R.id.buttonOkAmount)
            val editText: EditText = dialog.findViewById(R.id.editTextAmount)

            buttonOk.setOnClickListener{
                var newBet = 0
                try {
                    newBet = editText.text.toString().toInt()
                }
                catch (e: NumberFormatException) {
                    Toast.makeText(this, "Incorrect new bet", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                if(newBet > model.getScore() || newBet < 0) {
                    Toast.makeText(this, "Incorrect new bet", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                model.setBet(newBet)
                controllerTextViewAmount.text = getString(R.string.bet, newBet.toString())
                dialog.dismiss()
            }
            dialog.window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            dialog.show()
        }
    }

    private fun changeDice(number: Int, dice: Int){
        val background = when(number) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            6 -> R.drawable.dice_6
            else -> R.drawable.dice_1
        }

        when(dice){
            1 -> controllerImageViewDice1.setBackgroundResource(background)
            2 -> controllerImageViewDice2.setBackgroundResource(background)
            else -> controllerImageViewDice1.setBackgroundResource(background)
        }
    }
}