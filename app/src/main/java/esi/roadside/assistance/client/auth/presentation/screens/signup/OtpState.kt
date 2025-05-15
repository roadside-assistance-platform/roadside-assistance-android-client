package esi.roadside.assistance.client.auth.presentation.screens.signup

import esi.roadside.assistance.client.auth.presentation.OtpAction

data class OtpState(
    val code: List<Int?> = (1..6).map { null },
    val focusedIndex: Int? = null,
) {
    fun isCodeComplete(): Boolean {
        return code.all { it != null }
    }

    fun getCode(): String {
        return code.joinToString(separator = "") { it?.toString() ?: "" }
    }

    private fun getPreviousFocusedIndex(): Int? {
        return focusedIndex?.minus(1)?.coerceAtLeast(0)
    }

    private fun getFirstEmptyFieldIndexAfterFocusedIndex(
        currentFocusedIndex: Int
    ): Int {
        code.forEachIndexed { index, number ->
            if(index <= currentFocusedIndex) {
                return@forEachIndexed
            }
            if(number == null) {
                return index
            }
        }
        return currentFocusedIndex
    }

    private fun getNextFocusedTextFieldIndex(): Int? {
        if(focusedIndex == null) {
            return null
        }

        if(focusedIndex == code.size - 1) {
            return focusedIndex
        }

        return getFirstEmptyFieldIndexAfterFocusedIndex(currentFocusedIndex = focusedIndex)
    }

    fun onOtpAction(action: OtpAction): OtpState {
        return when(action) {
            is OtpAction.OnChangeFieldFocused -> {
                copy(focusedIndex = action.index)
            }

            is OtpAction.OnEnterNumber -> {
                enterNumber(action.number, action.index)
            }

            OtpAction.OnKeyboardBack -> {
                val previousIndex = getPreviousFocusedIndex()
                copy(
                    code = code.mapIndexed { index, number ->
                        if (index == previousIndex) {
                            null
                        } else {
                            number
                        }
                    },
                    focusedIndex = previousIndex
                )
            }
        }
    }

    private fun enterNumber(number: Int?, index: Int): OtpState {
        val newCode = code.mapIndexed { currentIndex, currentNumber ->
            if(currentIndex == index) {
                number
            } else {
                currentNumber
            }
        }
        val wasNumberRemoved = number == null
        return copy(
            code = newCode,
            focusedIndex =
                if (wasNumberRemoved || code.getOrNull(index) != null) focusedIndex
                else getNextFocusedTextFieldIndex()
        )
    }
}