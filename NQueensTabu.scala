import scala.annotation.tailrec

/**
 * Function to solve the n-Queens using Tabu algorithm
 * @ params n - Board dimensions (n x n) also n Queens
 * @ params numIterations - maximum number of iterations without find better solution
 * @ params tabuSize - Max length for tabu list
**/
def tabu(n: Int, numIterations: Int, tabuSize: Int): List[Int] = {

    val firstCombination: List[Int] = util.Random.shuffle((0 until n).toList)

    // Target function
    // Count the intersection numbers on the board
    def conflicts(board: List[Int]): Int = {
        val diag1 = board.zipWithIndex.map((c, r) => c + r)
        val diag2 = board.zipWithIndex.map((c, r) => c - r)

        def countConflicts(seq: List[Int]): Int = {
            seq.groupBy(identity).values.map { g =>
                val k = g.size
                if k > 1 then k * (k - 1) / 2 else 0
            }.sum
        }

        countConflicts(diag1) + countConflicts(diag2)
    }

    def randomNeighbors(board: List[Int], tabuList: List[(Int, Int)], n: Int): List[(Int, Int, List[Int])] = {
        def swap(board: List[Int], i: Int, j: Int): List[Int] =
            board.updated(i, board(j)).updated(j, board(i))

        val indices = util.Random.shuffle(
            for {
                i <- board.indices; 
                j <- (i + 1 until board.size) 
            } yield (i, j)
        )
        indices.filterNot(x => tabuList.contains(x)).take(n).map { case (i, j) => (i, j, swap(board, i, j)) }.toList
    }

    @tailrec
    def loop(
        current: List[Int], 
        best: Int, 
        tabuList: List[(Int, Int)], 
        bestCombination: List[Int],
        iterations: Int
    ): List[Int] = {

        if (conflicts(current) == 0 || iterations <= 0) {
            bestCombination
        }
        else {
            val neighbors = randomNeighbors(board = current, tabuList = tabuList, n = n)
            val (i, j, nextBoard) = neighbors.minBy {case (_, _, x) => conflicts(x)}
            
            val nextConf = conflicts(nextBoard)
            val (newBestComb, it) = if nextConf < best then (nextBoard, numIterations) else (bestCombination, iterations - 1)
            val newBest = best min nextConf

            val newTabu = ((i, j) :: tabuList).take(tabuSize)

            loop(nextBoard, newBest, newTabu, newBestComb, it)
        }
    }
    
    loop(
        current = firstCombination,
        best = conflicts(firstCombination),
        tabuList = Nil,
        bestCombination = firstCombination,
        iterations = numIterations
    )
}

@main def runNQueens(): Unit = {
  val n = 16
  val tabuSize = n - 1
  val maxIter = 100
  val solution = tabu(n, maxIter, tabuSize)

  println(s"Best solution by $n Queens:")
  println(solution)

  for row <- solution.indices do
    val line = (0 until n).map { col =>
      if solution(row) == col then "Q" else "."
    }.mkString(" ")
    println(line)
}
