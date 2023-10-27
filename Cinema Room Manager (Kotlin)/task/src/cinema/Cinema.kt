package cinema

class ViewingRoom(val rows: Int, val seatsPerRow: Int) {
    val roomSeats = MutableList(rows) { MutableList(seatsPerRow) { "S" } }
    val totalSeats = rows * seatsPerRow
    var bookedSeats = 0
    var currentIncome = 0
    val totalIncome = when {
        totalSeats <= 60 -> totalSeats * 10
        else -> rows / 2 * seatsPerRow * 10 + (rows - rows / 2) * seatsPerRow * 8
    }

    fun bookSeat(selectedRow: Int, selectedSeat: Int) {
        roomSeats[selectedRow - 1][selectedSeat - 1] = "B"
        bookedSeats++
        currentIncome += getTicketCost(selectedRow)
    }

    fun getTicketCost(selectedRow: Int): Int {
        return if (totalSeats <= 60 || selectedRow <= rows / 2) {
            10
        } else {
            8
        }
    }

    fun printSeatingChart() {
        val title = "\nCinema:"

        println(title)
        for (i in 0..roomSeats[0].size) {
            val colNumber = if (i == 0) " " else i.toString()

            print("$colNumber ")
        }
        println()

        for (rowIdx in roomSeats.indices) {
            println("${rowIdx + 1} " + roomSeats[rowIdx].joinToString(" "))
        }
    }

    fun isSeatAvailable(row: Int, seat: Int): Boolean {
        return roomSeats[row-1][seat-1] == "S"
    }
}

fun main() {
    println("Enter the number of rows:")
    val rows = readln().toInt()
    println("Enter the number of seats in each row:")
    val seatsPerRow = readln().toInt()

    val room = ViewingRoom(rows, seatsPerRow)

    var choice = -1
    while (choice != 0) {
        printMenu()
        choice = readln().toInt()

        when (choice) {
            1 -> room.printSeatingChart()
            2 -> buyTicket(room)
            3 -> printRoomStatistics(room)
        }
    }
}

fun printRoomStatistics(room: ViewingRoom) {
    val formatPercentage = try {
        val percentage = (room.bookedSeats * 100) / room.totalSeats.toDouble()
        "%.2f".format(percentage)
    } catch (e: Exception) {
        "0.00"
    }

    println()
    println(
        """
        Number of purchased tickets: ${room.bookedSeats}
        Percentage: $formatPercentage%
        Current income: $${room.currentIncome}
        Total income: $${room.totalIncome}
    """.trimIndent()
    )
}

fun buyTicket(room: ViewingRoom) {
    var validInput = false
    var selectedRow = 0
    var selectedSeat = 0

    while(!validInput) {
        println("\nEnter a row number:")
        selectedRow = readln().toInt()
        println("Enter a seat number in that row:")
        selectedSeat = readln().toInt()

        validInput = isInputValid(selectedRow, selectedSeat, room)
    }

    printTicketCost(selectedRow, room)
    room.bookSeat(selectedRow, selectedSeat)
}

fun isInputValid(selectedRow: Int, selectedSeat: Int, room: ViewingRoom): Boolean {
    if(selectedRow < 0 || selectedRow > room.rows || selectedSeat < 0 || selectedSeat > room.seatsPerRow){
        println("\nWrong input!")
        return false
    }

    return if(room.isSeatAvailable(selectedRow, selectedSeat)){
        true
    } else {
        println("\nThat ticket has already been purchased!")
        false
    }
}

fun printTicketCost(selectedRow: Int, room: ViewingRoom) {
    print("\nTicket price: ")
    println("$${room.getTicketCost(selectedRow)}")
}

fun printMenu() {
    println()
    println(
        """
        1. Show the seats
        2. Buy a ticket
        3. Statistics
        0. Exit
        """.trimIndent()
    )
}