;;; ***************************
;;; * DEFTEMPLATES & DEFFACTS *
;;; ***************************

(deftemplate UI-state
   (slot id (default-dynamic (gensym*)))
   (slot display)
   (slot relation-asserted (default none))
   (slot response (default none))
   (multislot valid-answers)
   (slot state (default middle)))
   
(deftemplate state-list
   (slot current)
   (multislot sequence))
  
(deffacts startup
   (state-list))
   
;;;****************
;;;* STARTUP RULE *
;;;****************

(defrule system-banner ""

  =>
  
  (assert (UI-state (display WelcomeMessage)
                    (relation-asserted start)
                    (state initial)
                    (valid-answers))))

;;;***************
;;;* QUERY RULES *
;;;***************

(defrule determine-is-scotland ""

   (logical (start))

   =>

   (assert (UI-state (display StartQuestion)
                     (relation-asserted scotland)
                     (response No)
                     (valid-answers No Yes))))


(defrule determine-sleep-double-wide ""

   (logical (scotland No))

   =>

   (assert (UI-state (display DoubleWideSleepQuestion)
                     (relation-asserted doubleWideSleep)
                     (response No)
                     (valid-answers No Yes))))


(defrule determine-is-Bob-or-Doug ""

   (logical (doubleWideSleep No))

   =>

   (assert (UI-state (display BobDougQuestion)
                     (relation-asserted isBoborDoug)
                     (response No)
                     (valid-answers No Yes))))


(defrule determine-live-in-projects ""

   (logical (isBoborDoug No))

   =>

   (assert (UI-state (display LiveInProjectsQuestion)
                     (relation-asserted liveInProjects)
                     (response No)
                     (valid-answers No Yes))))


(defrule determine-hockey-fan ""

   (logical (liveInProjects No))

   =>

   (assert (UI-state (display HockeyFanQuestion)
                     (relation-asserted hockeyFan)
                     (response No)
                     (valid-answers No Yes))))


(defrule determine-access-the-Stargate ""

   (logical (hockeyFan No))

   =>

   (assert (UI-state (display AccessTheStargateQuestion)
                     (relation-asserted theStargate)
                     (response No)
                     (valid-answers No Yes))))


(defrule determine-is-hipster ""

   (logical (theStargate No))

   =>

   (assert (UI-state (display IsHipsterQuestion)
                     (relation-asserted isHipster)
                     (response No)
                     (valid-answers No Yes))))


(defrule determine-is-designated-driver ""

   (logical (isHipster No))

   =>

   (assert (UI-state (display DesignatedDriverQuestion)
                     (relation-asserted designatedDriver)
                     (response No)
                     (valid-answers No Yes))))


(defrule determine-at-tailgate-frat-party ""

   (logical (designatedDriver No))

   =>

   (assert (UI-state (display TailgateOrFratPartyQuestion)
                     (relation-asserted tailgateFratParty)
                     (response No)
                     (valid-answers No Yes))))


(defrule determine-is-wife-nagging ""

   (logical (tailgateFratParty Yes))

   =>

   (assert (UI-state (display NaggingWifeQuestion)
                     (relation-asserted naggingWife)
                     (response No)
                     (valid-answers No Yes))))


(defrule determine-is-guinness ""

   (logical (tailgateFratParty No))

   =>

   (assert (UI-state (display GuinnessQuestion)
                     (relation-asserted guinness)
                     (response No)
                     (valid-answers No Yes))))


(defrule determine-is-summer ""

   (logical (guinness No))

   =>

   (assert (UI-state (display SummerQuestion)
                     (relation-asserted summer)
                     (response No)
                     (valid-answers No Yes))))


(defrule determine-speak-french-russian ""

   (logical (summer No))

   =>

   (assert (UI-state (display SpeakFrenchRussianQuestion)
                     (relation-asserted frenchInRussian)
                     (response No)
                     (valid-answers No Yes))))


(defrule determine-paying-in-change ""

   (logical (summer Yes))

   =>

   (assert (UI-state (display PayingInChangeQuestion)
                     (relation-asserted payingInChange)
                     (response No)
                     (valid-answers No Yes))))


(defrule determine-dinner-party ""

   (logical (frenchInRussian No))

   =>

   (assert (UI-state (display DinnerPartyQuestion)
                     (relation-asserted dinnerParty)
                     (response No)
                     (valid-answers No Yes))))


(defrule determine-graduation ""

   (logical (dinnerParty No))

   =>

   (assert (UI-state (display GraduationQuestion)
                     (relation-asserted graduation)
                     (response No)
                     (valid-answers No Yes))))


(defrule determine-is-parrothead ""

   (logical (graduation No))

   =>

   (assert (UI-state (display ParrotheadQuestion)
                     (relation-asserted parrothead)
                     (response No)
                     (valid-answers No Yes))))



(defrule determine-moved-to-parents ""

   (logical (graduation Yes))

   =>

   (assert (UI-state (display MovedToParentsQuestion)
                     (relation-asserted movedToParents)
                     (response No)
                     (valid-answers No Yes))))


(defrule determine-seem-worldly ""

   (logical (parrothead No))

   =>

   (assert (UI-state (display SeemWorldlyQuestion)
                     (relation-asserted seemWorldly)
                     (response No)
                     (valid-answers No Yes))))


(defrule determine-bringing-grocery-bag ""

   (logical (seemWorldly No))

   =>

   (assert (UI-state (display BringingGroceryBagQuestion)
                     (relation-asserted groceryBag)
                     (response No)
                     (valid-answers No Yes))))


(defrule determine-is-drunk ""

   (logical (groceryBag No))

   =>

   (assert (UI-state (display DrunkQuestion)
                     (relation-asserted drunk)
                     (response No)
                     (valid-answers No Yes))))


(defrule determine-beer-lover ""

   (logical (drunk No))

   =>

   (assert (UI-state (display BeerLoverQuestion)
                     (relation-asserted beerLover)
                     (response No)
                     (valid-answers No Yes))))


(defrule determine-expensive-love ""

   (logical (beerLover Yes))

   =>

   (assert (UI-state (display ExpensiveQuestion)
                     (relation-asserted expensiveLove)
                     (response No)
                     (valid-answers No Yes))))



;;;****************
;;;* BEER RULES *
;;;****************

(defrule is-scotland ""

   (logical (scotland Yes))
   
   =>

   (assert (UI-state (display Scotland)
                     (state final))))

(defrule sleep-double-wide ""

   (logical (doubleWideSleep Yes))
   
   =>

   (assert (UI-state (display DoubleWideSleep)
                     (state final))))

(defrule is-Bob-or-Doug  ""

   (logical (isBoborDoug Yes))
   
   =>

   (assert (UI-state (display BoborDoug)
                     (state final))))

(defrule live-in-projects  ""

   (logical (liveInProjects Yes))
   
   =>

   (assert (UI-state (display BoborDoug)
                     (state final))))

(defrule hockey-fan  ""

   (logical (hockeyFan Yes))
   
   =>

   (assert (UI-state (display HockeyFan)
                     (state final))))

(defrule access-the-stargate  ""

   (logical (theStargate Yes))
   
   =>

   (assert (UI-state (display TheStargate)
                     (state final))))

(defrule access-is-hipster  ""

   (logical (isHipster Yes))
   
   =>

   (assert (UI-state (display Hipster)
                     (state final))))

(defrule is-designated-driver  ""

   (logical (designatedDriver Yes))
   
   =>

   (assert (UI-state (display DesignatedDriver)
                     (state final))))

(defrule nagging-wife  ""

   (logical (naggingWife Yes))
   
   =>

   (assert (UI-state (display NaggingWife)
                     (state final))))

(defrule no-nagging-wife  ""

   (logical (naggingWife No))
   
   =>

   (assert (UI-state (display NoNaggingWife)
                     (state final))))

(defrule is-guinness  ""

   (logical (guinnesss Yes))
   
   =>

   (assert (UI-state (display Guinness)
                     (state final))))


(defrule paying-in-change  ""

   (logical (payingInChange Yes))
   
   =>

   (assert (UI-state (display PayingInChange)
                     (state final))))



(defrule no-paying-in-change  ""

   (logical (payingInChange No))
   
   =>

   (assert (UI-state (display NoPayingInChange)
                     (state final))))

(defrule speak-french-russian  ""

   (logical (frenchInRussian Yes))
   
   =>

   (assert (UI-state (display FrenchInRussian)
                     (state final))))

(defrule dinner-party  ""

   (logical (dinnerParty Yes))
   
   =>

   (assert (UI-state (display DinnerParty)
                     (state final))))


(defrule moved-to-parents  ""

   (logical (movedToParents Yes))
   
   =>

   (assert (UI-state (display MovedToParents)
                     (state final))))


(defrule no-moved-to-parents  ""

   (logical (movedToParents No))
   
   =>

   (assert (UI-state (display NoMovedToParents)
                     (state final))))


(defrule is-parrothead  ""

   (logical (parrothead Yes))
   
   =>

   (assert (UI-state (display Parrothead)
                     (state final))))

(defrule seem-worldly  ""

   (logical (seemWorldly Yes))
   
   =>

   (assert (UI-state (display SeemWorldly)
                     (state final))))


(defrule bringing-grocery-bag  ""

   (logical (groceryBag Yes))
   
   =>

   (assert (UI-state (display GroceryBag)
                     (state final))))


(defrule is-drunk  ""

   (logical (drunk Yes))
   
   =>

   (assert (UI-state (display Drunk)
                     (state final))))


(defrule beer-lover  ""

   (logical (beerLover No))
   
   =>

   (assert (UI-state (display NoBeerLover)
                     (state final))))


(defrule no-expensive-love  ""

   (logical (expensiveLove No))
   
   =>

   (assert (UI-state (display NoExpensiveLove)
                     (state final))))


(defrule expensive-love  ""

   (logical (expensiveLove Yes))
   
   =>

   (assert (UI-state (display ExpensiveLove)
                     (state final))))



                     
;;;*************************
;;;* GUI INTERACTION RULES *
;;;*************************

(defrule ask-question

   (declare (salience 5))
   
   (UI-state (id ?id))
   
   ?f <- (state-list (sequence $?s&:(not (member$ ?id ?s))))
             
   =>
   
   (modify ?f (current ?id)
              (sequence ?id ?s))
   
   (halt))

(defrule handle-next-response-none-end-of-chain

   (declare (salience 10))
   
   ?f <- (next ?id)

   (state-list (sequence ?id $?))
   
   (UI-state (id ?id)
             (relation-asserted ?relation))
                   
   =>
      
   (retract ?f)

   (assert (add-response ?id)))   
   
(defrule handle-next-response-end-of-chain

   (declare (salience 10))
   
   ?f1 <- (next ?id ?response)
   
   (state-list (sequence ?id $?))
   
   ?f2 <- (UI-state (id ?id)
                    (response ?expected)
                    (relation-asserted ?relation))
                
   =>
      
   (retract ?f1)

   (if (neq ?response ?expected)
      then
      (modify ?f2 (response ?response)))
      
   (assert (add-response ?id ?response)))   

(defrule handle-add-response

   (declare (salience 10))
   
   (logical (UI-state (id ?id)
                      (relation-asserted ?relation)))
   
   ?f1 <- (add-response ?id ?response)
                
   =>
      
   (str-assert (str-cat "(" ?relation " " ?response ")"))
   
   (retract ?f1))   

(defrule handle-add-response-none

   (declare (salience 10))
   
   (logical (UI-state (id ?id)
                      (relation-asserted ?relation)))
   
   ?f1 <- (add-response ?id)
                
   =>
      
   (str-assert (str-cat "(" ?relation ")"))
   
   (retract ?f1))   