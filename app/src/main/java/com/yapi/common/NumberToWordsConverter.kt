package com.yapi.common

class NumberToWordsConverter {
    companion object {
        private val units = arrayOf("Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten",
            "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen")
        private val tens = arrayOf("", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety")

        fun convert(number: Int): String {
            if (number < 20) {
                return units[number]
            }

            if (number < 100) {
                return "${tens[number / 10]}${if (number % 10 != 0) " " else ""}${units[number % 10]}"
            }

            if (number < 1000) {
                return "${units[number / 100]} Hundred${if (number % 100 != 0) " " else ""}${convert(number % 100)}"
            }

            if (number < 1000000) {
                return "${convert(number / 1000)} Thousand${if (number % 1000 != 0) " " else ""}${convert(number % 1000)}"
            }

            if (number < 1000000000) {
                return "${convert(number / 1000000)} Million${if (number % 1000000 != 0) " " else ""}${convert(number % 1000000)}"
            }

            return "${convert(number / 1000000000)} Billion${if (number % 1000000000 != 0) " " else ""}${convert(number % 1000000000)}"
        }
    }
}