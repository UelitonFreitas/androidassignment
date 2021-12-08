package com.adyen.android.assignment

import com.adyen.android.assignment.money.Bill
import com.adyen.android.assignment.money.Change
import com.adyen.android.assignment.money.Coin
import org.junit.Assert.assertEquals
import org.junit.Test

class CashRegisterTest {

    @Test
    fun `should have initial value`() {
        val expectedChangeValue = Change().add(Coin.TWO_EURO, 1)

        val cashRegister = CashRegister(expectedChangeValue)
        cashRegister.performTransaction(0L, Change())

        assertEquals(expectedChangeValue, cashRegister.total)
    }

    @Test(expected = CashRegister.TransactionException::class)
    fun `should not make transaction when price is negative`() {
        val cashRegister = CashRegister(Change().add(Coin.TWO_EURO, 1))

        cashRegister.performTransaction(-1L, Change())
    }

    @Test(expected = CashRegister.TransactionException::class)
    fun `should not make a transaction if the change is less than product price`() {
        val cashRegister = CashRegister(Change().add(Coin.TWO_EURO, 1))

        cashRegister.performTransaction(10L, Change().add(Coin.ONE_CENT, 1))
    }

    @Test
    fun `should not return change when money paid is same as price`() {
        val noChange = Change()

        val cashRegister = CashRegister(Change().add(Bill.ONE_HUNDRED_EURO, 1))

        val transactionResult =
            cashRegister.performTransaction(10_00, Change().add(Bill.TEN_EURO, 1))

        assertEquals(noChange, transactionResult)
    }

    @Test
    fun `should return change`() {
        val fiftyCentChange = Change().add(Coin.FIFTY_CENT, 1)

        val cashRegister = CashRegister(
            Change().add(Bill.TEN_EURO, 1)
        )

        val transactionResult = cashRegister.performTransaction(
            10_50, Change()
                .add(Bill.TEN_EURO, 1)
                .add(Coin.FIFTY_CENT, 2)
        )

        assertEquals(fiftyCentChange, transactionResult)
    }

    @Test(expected = CashRegister.TransactionException::class)
    fun `should not make a transaction when there is no enough change`() {

        val cashRegister = CashRegister(Change().add(Bill.TEN_EURO, 1))

        cashRegister.performTransaction(5_00, Change().add(Bill.TEN_EURO, 1))
    }

    @Test
    fun `should update cash register with multiples transactions`() {
        val expectedFiftyCentChangeForTransaction1 = Change().add(Coin.FIFTY_CENT, 1)
        val expectedTotalInCashRegisterForTransaction1 = Change()
            .add(Coin.FIFTY_CENT, 1)
            .add(Bill.TEN_EURO, 2)

        val expectedFiftyCentChangeForTransaction2 = Change().add(Coin.FIFTY_CENT, 1)
        val expectedTotalInCashRegisterForTransaction2 = Change()
            .add(Bill.TEN_EURO, 2)
            .add(Coin.ONE_EURO, 2)

        val cashRegister = CashRegister(
            Change().add(Bill.TEN_EURO, 1)
        )

        val fiftyCentChangeTransactionResult = cashRegister.performTransaction(
            10_50, Change()
                .add(Bill.TEN_EURO, 1)
                .add(Coin.FIFTY_CENT, 2)
        )

        assertEquals(expectedFiftyCentChangeForTransaction1, fiftyCentChangeTransactionResult)
        assertEquals(expectedTotalInCashRegisterForTransaction1, cashRegister.total)

        val fiftyCentChangeTransactionResult2 = cashRegister.performTransaction(
            1_50, Change().add(Coin.ONE_EURO, 2)
        )

        assertEquals(expectedFiftyCentChangeForTransaction2, fiftyCentChangeTransactionResult2)
        assertEquals(expectedTotalInCashRegisterForTransaction2, cashRegister.total)
    }
}
