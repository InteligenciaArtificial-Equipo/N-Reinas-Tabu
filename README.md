# N-Reinas-Tabu
Problema de las 8 reinas resuelto con el algoritmo Tabu
Leguaje de programacion Scala3
Solucion Funcional Pura

## Integrantes
Peñuelas López Luis Antonio
Peraza Medina Eliezer Daniel

## Explicacion

### Funcion Tabu
Usada para resolver el problema, recive tres parametros
- *n* -> Dimensiones del tablero y numero de Reinas
- *numIterations* -> El maximo numero de iteracion a seguir buscando mientras no se encuentre una mejor solucion
- *tabuSize* -> El maximo largo de la lista tabu

``` Scala
def tabu(n: Int, numIterations: Int, tabuSize: Int): List[Int] = ???

```

Dentro de la funcion de definen mas funciones que se usan como parte del tabu

#### Procedimiento
- Generar una primera solucion aleatoria (Para cada reina se le asigna una columna aleatoria, todas diferentes).
- Se evalua la primera solucion y se toma como la mejor.
- Se verifica si cumple la condicion de terminacion, si la cumple retorna la mejor solucion encontrada:
``` Scala
if (conflicts(current) == 0 || iterations <= 0) {
  bestCombination
}
```
- Se genera el vecindario.
- Se evalua cada vecino y se toma el mejor (en este caso el mejor es el que tiene menos coliciones).
``` Scala
val neighbors = randomNeighbors(board = current, tabuList = tabuList, n = n)
val (i, j, nextBoard) = neighbors.minBy {case (_, _, x) => conflicts(x)}
```
- Se verifica si el vecino mejora la solucion actual de ser asi se toma como el mejor y se reinicia la cuenta de iteraciones restantes.
- Se continua explorando apartir del nuevo vecino. 

### Funcion Objetivo
La funcion objetivo evalua la solucion, el resultado de para un tablero dado es la cantidad de coliciones entre las reinas
Siendo un resultado de 0 un tablero con el acomodo perfecto (Una solucion optima)

``` Scala
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
```
Ya que cada reina esta en una columna y fila diferente, solo puede haber coliciones en las diagonales, las cuales de clasifican en diagonales derechas e izquierdas.
Para las diagonales izquierdas se pueden identificar con la suma de la columna y la fila.
Para las diagonales derechas se pueden identicar con la diferencia entre columna y fila.

*Ejemplo*
(0, 0), (1, 1), (2, 2) -> Diferencia es 0, todas estas casillas estan en la misma diagonal.

### Generacion del vecindario

La funcion Genera un vecindario aleatorip de largo *n* omitiendo los vecinos que estan en la lista tabu ya que no deben ser considerados.
Los veciones parten de un tablero actual y se generan haciendo un unico swap entre 2 elementos.

``` Scala
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
```

## Ejecucion

### Para ejecutar
``` bash
scala-cli run NQueensTabu.scala
```

### Evidencia de ejecucion
<img width="630" height="466" alt="image" src="https://github.com/user-attachments/assets/bf36071f-cc43-4637-9622-5b1ed881fe7f" />
