package chapter3

fun main() {
    val list = List.of(1.1, 0.0, 2.2, 3.3, 4.4, 5.5)
    val fr = foldRight(list, 0.0, { a: Double, b: Double -> a + b })

    println(fr)
}

//3.1
fun <A> tail(xs: List<A>): List<A> = when (xs) {
    is Nil -> throw IllegalStateException("tail called on empty list")
    is Cons -> xs.tail
}

//3.2
fun <A> setHead(xs: List<A>, x: A): List<A> = when (xs) {
    is Nil -> throw IllegalStateException("setHead called on empty list")
    is Cons -> Cons(x, xs.tail)
}

//3.3
fun <A> drop(l: List<A>, n: Int): List<A> = when (l) {
    is Nil -> throw IllegalStateException("drop called on empty list")
    is Cons -> if (n == 0) l else drop(l.tail, n - 1)
}

//3.4
fun <A> dropWhile(l: List<A>, f: (A) -> Boolean): List<A> = when (l) {
    is Nil -> throw IllegalStateException("dropWhile called on empty list")
    is Cons -> if (f(l.head)) dropWhile(l.tail, f) else l
}

//3.5
fun <A> init(l: List<A>): List<A> = when (l) {
    is Nil -> throw IllegalStateException("init called on empty list")
    is Cons -> when (l.tail) {
        is Nil -> Nil
        is Cons -> Cons(l.head, init(l.tail))
    }
}

//3.6
fun <A, B> foldRight2(l: List<A>, z: B, f: (A, B) -> B): B = when (l) {
    is Nil -> z
    is Cons -> {
        when (l.head) {
            is Double -> {
                if (l.head == 0.0) {
                    z
                } else {
                    f(l.head, foldRight2(l.tail, z, f))
                }
            }

            else -> {
                f(l.head, foldRight2(l.tail, z, f))
            }
        }
    }
}

//3.7

//3.8
fun <A> length(xs: List<A>): Int = foldRight(xs, 0) { _, b -> b + 1 }

//3.9
tailrec fun <A, B> foldLeft(xs: List<A>, z: B, f: (B, A) -> B): B = when (xs) {
    is Nil -> z
    is Cons -> {
        println("foldingLeft: " + f(z, xs.head))
        foldLeft(xs.tail, f(z, xs.head), f)
    }
}

//3.10
fun sum2(ints: List<Int>): Int = foldLeft(ints, 0) { a, b -> a + b }
fun product2(ds: List<Double>): Double = foldLeft(ds, 1.0) { a, b -> a * b }

//3.11
fun <A> reverse(l: List<A>): List<A> = foldLeft(l, List.of()) { a, b -> Cons(b, a) }
fun <A> reverse2(l: List<A>): List<A> = foldRight(l, List.of()) { a, b -> append(b, List.of(a)) }

//3.12
fun <A, B> foldRightByLeft(xs: List<A>, z: B, f: (A, B) -> B): B = when (xs) {
    is Nil -> z
    is Cons -> foldLeft(xs, z) { a, b -> f(b, a) }

}

fun <B, A> foldLeftByRight(xs: List<A>, z: B, f: (B, A) -> B): B = when (xs) {
    is Nil -> z
    is Cons -> foldRight(xs, z) { a, b -> f(b, a) }
}

tailrec fun <A, B> foldLeft2(xs: List<A>, z: B, f: (B, A) -> B): B = when (xs) {
    is Nil -> z
    is Cons -> foldLeft(xs.tail, f(z, xs.head), f)
}

//3.13
fun <A> appendByFoldRight(a1: List<A>, a2: List<A>): List<A> = foldRight(a1, a2) { a, b -> Cons(a, b) }
fun <A> appendByFoldLeft(a1: List<A>, a2: List<A>): List<A> = foldLeft(reverse(a1), a2) { b, a -> Cons(a, b)}


fun <A, B> foldRight(xs: List<A>, z: B, f: (A, B) -> B): B = when (xs) {
    is Nil -> z
    is Cons -> f(xs.head, foldRight(xs.tail, z, f))
}

//3.14
fun <A> flatList(lists: List<List<A>>): List<A> = foldRight(lists, List.of()) {a, b -> append(a, b)}

//3.15
fun plusOneList(list: List<Int>): List<Int> = foldRight(list, List.of()) { a, b -> Cons(a + 1, b) }

//3.16
fun doubleToString(list: List<Double>): List<String> = foldRight(list, List.of()) { a, b -> Cons(a.toString(), b) }

//3.17
fun <A, B> map(list: List<A>, f: (A) -> B): List<B> = foldRight(list, List.of()) {a, ls -> Cons(f(a), ls)}

//3.18
fun <A> filter(xs: List<A>, f: (A) -> Boolean): List<A> = foldRight(xs, List.of()) {
    a: A, list: List<A> -> if(f(a)) Cons(a, list) else list
}

//3.19
fun <A, B> flatMap(xa: List<A>, f:(A) -> List<B>): List<B> = foldRight(xa, List.of()) {a, b -> append(f(a), b)}

// 3.20
fun <A> filterByFlatMap(xs: List<A>, f: (A) -> Boolean): List<A> =


fun <A> empty(): List<A> = Nil
sealed class List<out A> {
    companion object {
        fun <A> of(vararg aa: A): List<A> {
            val tail = aa.sliceArray(1 until aa.size)
            return if (aa.isEmpty()) Nil else Cons(aa[0], of(*tail))
        }
    }
}

object Nil : List<Nothing>()
data class Cons<out A>(val head: A, val tail: List<A>) : List<A>()

val ex1: List<Double> = Nil

fun sum(ints: List<Int>): Int = when (ints) {
    is Nil -> 0
    is Cons -> ints.head + sum(ints.tail)
}

fun product(ds: List<Double>): Double = when (ds) {
    is Nil -> 1.0
    is Cons -> if (ds.head == 0.0) 0.0 else ds.head * product(ds.tail)
}

fun <A> append(a1: List<A>, a2: List<A>): List<A> = when (a1) {
    is Nil -> a2
    is Cons -> Cons(a1.head, append(a1.tail, a2))
}

