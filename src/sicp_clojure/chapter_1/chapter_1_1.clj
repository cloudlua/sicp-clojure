(ns sicp-clojure.chapter-1.chapter-1-1
  (:require [clojure.math.numeric-tower :as math]))

;Exercise 1.1.  Below is a sequence of expressions. What is the result printed by the interpreter in response to each expression? Assume that the sequence is to be evaluated in the order in which it is presented.

10 ;10
(+ 5 3 4) ;12
(- 9 1) ;8
(/ 6 2) ;3
(+ (* 2 4) (- 4 6)) ;6
(def a 3) ;#'sicp-clojure.chapter-1.chapter-1-1/a
(def b (+ a 1)) ;#'sicp-clojure.chapter-1.chapter-1-1/b
(+ a b (* a b)) ;19
(= a b) ;false
(if (and (> b a) (< b (* a b)))
    b
    a) ;4
(cond (= a 4) 6
      (= b 4) (+ 6 7 a)
      :else 25) ;16
(+ 2 (if (> b a) b a)) ;6
(* (cond (> a b) a
         (< a b) b
         :else -1)
   (+ a 1)) ;16

;Exercise 1.2. (picture)

(/ (+ 5
      4
      (- 2
         (- 3
            (+ 6
               1/5))))
   (* 3
      (- 6
         2)
      (- 2
         7))) ;-71/300 (ratio)

;Exercise 1.3.  Define a procedure that takes three numbers as arguments and returns the sum of the squares of the two larger numbers.
;Only uses functions and forms we know so far

(defn square [x] (* x x))
(defn square-sum [x y]
  (+ (square x)
     (square y)))
;don't want to shadow the core max function
(defn max-of-two [x y]
  (if (> x y) x y))

(defn square-sum-two-largest [x y z]
  (if (= x (max-of-two x y))
         (square-sum x (max-of-two y z))
         (square-sum y (max-of-two x z))))

;Exercise 1.4.  Observe that our model of evaluation allows for combinations whose operators are compound expressions. Use this observation to describe the behavior of the following procedure:

(defn a-plus-abs-b [a b]
  ((if (> b 0) + -) a b))

;You can choose a computation function based on the arguments.

;Exercise 1.5.  Ben Bitdiddle has invented a test to determine whether the interpreter he is faced with is using applicative-order evaluation or normal-order evaluation. He defines the following two procedures:

(defn p [] 
  (p))

(defn test [x y]
  (if (= x 0)
      0
      y))

;Then he evaluates the expression

(test 0 (p))

;Stack oveflow with applicative order, with normal order it would return 0.


;Definition of sqrt in Clojure
(defn square [x] (* x x))

(defn average [x y]
  (/ (+ x y) 2))

(average 1 2)

(defn good-enough? [guess x]
  (< (java.lang.Math/abs (- (square guess) x))
     0.000000000001))

(good-enough? 1.0000001 1.0000002)

(defn improve [guess x]
  (average guess (/ x guess)))
  
(improve 577/408 2)

(defn sqrt-iter [guess x]
  (if (good-enough? guess x)
      guess 
      (recur (improve guess x)
                 x)))

(sqrt-iter 1 2.0)


;Lectures 1b.1
(defn peano-plus [x y]
  (if (= x 0)
    y
    (recur (dec x) (inc y))))

(peano-plus 13 4)

;Lectures 1b.2
(defn peano-plus [x y]
  (if (= x 0)
    y
    (inc (peano-plus (dec x)  y))))

(peano-plus 13 4)

;Lectures 1b.3
(defn fib [n]
  (if (< n 2)
    n
    (+ (fib (- n 1))
       (fib (- n 2)))))

(fib 30)

(defn print-move [from to]
  (println "Move top from" from "to" to))


(defn move [n from to spare]
  (if (= n 0) "DONE"
      (do (move (dec n) from spare to)
          (print-move from to)
          (move (dec n) spare to from))))
        
(move 10 "Prvi" "Končni" "Pomožni")  
        
;Lectures 2a.1
(defn sum-int [a b]
  (if (> a b)
    0
    (+ a
       (sum-int (inc a) b))))

(sum-int 1 5000)        
        
(defn sum-sq [a b]
  (if (> a b)
    0
    (+ (square a)
       (sum-sq (inc a) b))))

(sum-sq 1 10)        
        
(defn sum [term a next b]
  (if (> a b)
    0
    (+ (term a)
       (sum term
            (next a)
            next
            b))))

(sum identity 1 inc 4)

(sum #(* % %) 1 #(+ % %) 4)

(def pi8 
  (sum #(/ 1 (* % (+ % 2)))
     1
     #(+ % 4)
     5000))

(* (double pi8) 8)

;Lectures 2a.2
(defn fixed-point [f start]
  (let [tolerance 0.0000000000001
        close-enuf? (fn [u v] (< (math/abs (- u v)) tolerance))]
	  (loop [old start
          new (f start)]
	    (if (close-enuf? old new)
         new
         (recur new (f new))))))
	    

(defn sqrt [x]
  (fixed-point #(average (/ x %) %)
               1))
        
(double (sqrt 2.0))
        

(defn sqrt [x]
  (fixed-point 
    (average-damp #(/ x %))
    1))
       
(defn average-damp [f]
  (fn [x] (average (f x) x)))


;Lectures 2a.3
(defn sqrt [x]
  (newton #(- x (square %))
          1))

(defn newton [f guess]
  (let [df (deriv f)]
    (fixed-point #(- %
                    (/ (f %)
                       (df %)))
                 guess)))

(defn deriv [f]
  (fn [x] 
    (/ (- (f (+ x dx)) 
          (f x))
        dx)))

(def dx 0.000001)
