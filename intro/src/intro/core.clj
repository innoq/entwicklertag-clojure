(ns intro.core)

                                        ; Skalare Datentypen

;; Zahlen
123
3.45
1/3

;; Strings
"Hello, World!"

;; Characters
\a
\b
\c

;; Keywords
:key

;; reguläre Ausdrücke
#"reg.*ex.*"

;; Symbols
;; a -> Exception "unable to resolve symbol"
(quote a)

                                        ; Collections

;; Vektoren
[1 2 3]
[:a "b" \c [:d #"e"]]

;; Maps
{:a 1
 :b 2}

;; Sets
#{:a :b :c}
;; #{:unique :unique} -> IllegalArgumentException

;; Listen
;; (1 2 3)

;; alles lässt sich schachteln
{:vector [1 2 3]
 :set #{:unique :items}
 :nested-map {:more ["things" :in 'here]}}

                                        ; Funktionen

;; Syntax => (operator arg1 arg2 arg3)
(+ 1 2 3)

(count [1 2 3])

(inc 5)

(if true
  :foo
  :bar)

;; Namen vergeben (im aktuellen Namespace)
(def my-set #{1 2 3})

;; Vektoren sind Funktionen ihrer Indices
(def v [:lots :of :stuff])
(v 0) ; -> :lots
;; (v 5) -> IndexOutOfBoundsException

;; Maps sind Funktionen
(def people {:pg "Phillip Ghadir" :st "Stefan Tilkov"})
(people :st) ; -> Stefan Tilkov

;; Keywords sind Funktionen
(:pg people) ;-> Phillip Ghadir
(:fred people) ; -> nil

;; Formatieren von Strings
(format "Hello, %s # %d" "world" 1)

;; Anwenden einer Funktion auf Collection von Argumenten
(apply format ["Hello, %s # %d" "world" 2])

                                        ; Daten sind unveränderlich

(def stuff [1 2 3])

(conj stuff 4) ;-> [1 2 3 4]

stuff ;-> [1 2 3]

(assoc {:a 1} :b 2) ;-> {:a 1 :b 2}

                                        ; Definieren von Funktionen

;; anonyme Funktion
(fn [x] (format "The value is %s" x))

;; anonyme Funktion anwenden
((fn [x] (format "The value is %s" x)) :foo)

;; benannte Funktion
(def testfn (fn [x] (format "The value is %s" x)))
(testfn "Hello")

;; kürzere Schreibweise
(defn testfn [x] (format "The value is %s" x))
(testfn "Hello")

;; Beispielfunktion
(defn even-num? [num]
  (= 0 (rem num 2)))

(even-num? 4) ;; true

;; Funktionen sind first-class 1
(def even-alias even-num?)
(even-alias 2) ;; true

;; Funktionen sind first-class 2
(defn every-even? [col]
  (every? even-num? col))

(every-even? '(2 4 6 8 9)) ;; false

;; anonyme Funktion
(every? (fn [x] (= 0 (rem x 2)))
        '(2 4 6 8 9)) ;; false

;; Reader-Macro für anonyme Funktionen
(every? #(= 0 (rem % 2))
        '(2 4 6 8 9)) ;; false

                                        ; wichtige Higher Order Functions

;; Dokumentation finden:
;; - (doc <name>)
;; - (find-doc "<string>")
;; - (apropos "<string-or-pattern>")

(map inc [1 2 3]) ;; (2 3 4)

(filter even-num? (range 10)) ;; (0 2 4 6 8)

(reduce + 0 [1 2 3 4]) ;; 10

;; Namen mit lokalem Scope
(defn sum-of-ages
  "takes things with an age and returns the sum of the ages"
  [persons]
  (let [ages (map :age persons)]
    (reduce + 0 ages)))

;; Funktionen sind Closures
(defn make-counter [initial-value]
  (let [current-value (atom initial-value)]
    (fn []
      (swap! current-value inc))))

(def counter1 (make-counter 0))
(counter1) ;; 1
(counter1) ;; 2

(def counter2 (make-counter 17))
(counter1) ;; 3
(counter2) ;; 18
(counter1) ;; 4
(counter2) ;; 19

                                        ; Collection-spezifische Funktionen

(conj [1 2 3] 4)

(conj (list 1 2 3) 4)

(conj #{1 2 3} 4)

(conj {:a 1 :b 2 :c 3} [:d 4])

(count [1 2 3])

(count {:a 1 :b 2 :c 3})

                                        ; Sequences

;; Clojures Collection-Library arbeitet mit Sequence-Abstraktion (first, rest, cons)

(first [1 2 3]) ;-> 1

(rest [1 2 3]) ;-> (2 3)

(cons 0 [1 2 3]) ;-> (0 1 2 3)

;; Factory für Sequences
(seq [1 2 3])

;; funktioniert mit allem, was java.lang.Iterable implementiert (und Strings und Arrays)
(def array-list (java.util.ArrayList.))
(.add array-list 1)
(.add array-list 2)
(.add array-list 3)
(seq array-list)

(map inc array-list)

(comment
(with-open [rdr (clojure.java.io/reader "project.clj")]
  (let [lines (line-seq rdr)
        nums (iterate inc 1)]
    (doseq [[n l] (map vector nums lines)]
      (println n "->" l))))
)

;; empty?, partition, take, drop, iterate, cycle

                                        ; Lazy Seqs

(defn tracing-inc [num]
  (println "incrementing" num)
  (inc num))

;; Clojure Collection Functions geben i.A. lazy Sequences zurück
(map tracing-inc [1 2 3]) ;; -> Berechnung erfolgt erst, wenn das Ergebnis ausgegeben werden soll

(take 10 (iterate tracing-inc 1))

;; Rekursion
(defn map-1 [f coll]
  (if (empty? coll)
    coll
    (cons
     (f (first coll))
     (map-1 f (rest coll)))))

;; Laziness
(defn map-2 [f coll]
  (lazy-seq
   (if (empty? coll)
     coll
     (cons
      (f (first coll))
      (map-2 f (rest coll))))))

;; Tail-Recursion
(defn reduce-1 [f acc coll]
  (if (empty? coll)
    acc
    (reduce-1 f
              (f acc (first coll))
              (rest coll))))

;; loop/recur
(defn reduce-2 [f val coll]
  (if (empty? coll)
    val
    (recur f
           (f val (first coll))
           (rest coll))))


;; Pipes/Threading
(-> [1 2 3]
    (conj 4)
    (conj 5))

(conj (conj [1 2 3] 4) 5)

(->> [1 2 3]
     (map inc)
     (filter even?))

;; Destructuring
(let [[a b] [1 2]]
  (format "a is %s and b is %s" a b))

(let [[a b & more] (range 10)]
  (format "a is %s, b is %s and more is %s" a b more))

(let [{n :name} {:name "Hannes" :age 45}]
  (format "n is %s" n))

(let [{:keys [name]} {:name "Hannes" :age 45}]
  (format "name is %s" name))

;; optionale Argumente
(defn foo [& {:keys [x y] :or {x 1 y 2}}]
  [x y])

