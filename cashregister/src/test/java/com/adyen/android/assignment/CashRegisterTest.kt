package com.adyen.android.assignment

import com.adyen.android.assignment.money.Change
import com.adyen.android.assignment.money.Coin
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

class CashRegisterTest {
    @Test
    fun `should have initial value`() {
        val expectedChangeValue = Change().add(Coin.TWO_EURO, 1)

        val cashRegister = CashRegister(expectedChangeValue)

        assertEquals(expectedChangeValue, cashRegister.performTransaction(0L, Change()))
    }
}
