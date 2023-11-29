package com.example.chohan

class ChoHanGameModel(private val startScore: Int, private val startBet: Int) {
    private var score = startScore
    private var bet = startBet
    private var firstDice: Int = 1
    private var secondDice: Int = 1
    private var choice: Choice = Choice.NONE
    fun throwDice() {
        this.firstDice = (1..6).random()
        this.secondDice = (1..6).random()
    }

    fun placeBet() {
        if(this.score - this.bet < 0) {
            this.bet = this.score
        }
        this.score -= this.bet
    }

    fun receiveWinnings() {
        this.score += (this.bet * 2)
    }

    fun reset() {
        this.score = this.startScore
        this.bet = this.startBet
    }

    fun setBet(newBet: Int) {
        this.bet = newBet
    }

    fun setChoice(choice: Choice) {
        this.choice = choice
    }

    fun getScore(): Int {
        return this.score
    }

    fun getBet(): Int {
        return this.bet
    }

    fun getFirstDice(): Int {
        return this.firstDice
    }

    fun getSecondDice(): Int {
        return this.secondDice
    }

    fun getResult(): Result {
        val diceSum = if((this.firstDice + this.secondDice) % 2 == 0) {
            Choice.EVEN
        }
        else {
            Choice.ODD
        }

        return if(diceSum == this.choice) {
            Result.WON
        } else {
            Result.LOST
        }
    }
}