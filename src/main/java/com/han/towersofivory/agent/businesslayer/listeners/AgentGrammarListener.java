// Generated from /home/plusplus/Projects/asd-project-s2-2024-klas-b/src/main/java/com/han/towersofivory/agent/businesslayer/AgentGrammar.g4 by ANTLR 4.13.1
package com.han.towersofivory.agent.businesslayer.listeners;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link AgentGrammarParser}.
 */
public interface AgentGrammarListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link AgentGrammarParser#agent}.
	 * @param ctx the parse tree
	 */
	void enterAgent(AgentGrammarParser.AgentContext ctx);
	/**
	 * Exit a parse tree produced by {@link AgentGrammarParser#agent}.
	 * @param ctx the parse tree
	 */
	void exitAgent(AgentGrammarParser.AgentContext ctx);
	/**
	 * Enter a parse tree produced by {@link AgentGrammarParser#regel}.
	 * @param ctx the parse tree
	 */
	void enterRegel(AgentGrammarParser.RegelContext ctx);
	/**
	 * Exit a parse tree produced by {@link AgentGrammarParser#regel}.
	 * @param ctx the parse tree
	 */
	void exitRegel(AgentGrammarParser.RegelContext ctx);
	/**
	 * Enter a parse tree produced by {@link AgentGrammarParser#expressie}.
	 * @param ctx the parse tree
	 */
	void enterExpressie(AgentGrammarParser.ExpressieContext ctx);
	/**
	 * Exit a parse tree produced by {@link AgentGrammarParser#expressie}.
	 * @param ctx the parse tree
	 */
	void exitExpressie(AgentGrammarParser.ExpressieContext ctx);
	/**
	 * Enter a parse tree produced by {@link AgentGrammarParser#omgevingssituatie}.
	 * @param ctx the parse tree
	 */
	void enterOmgevingssituatie(AgentGrammarParser.OmgevingssituatieContext ctx);
	/**
	 * Exit a parse tree produced by {@link AgentGrammarParser#omgevingssituatie}.
	 * @param ctx the parse tree
	 */
	void exitOmgevingssituatie(AgentGrammarParser.OmgevingssituatieContext ctx);
	/**
	 * Enter a parse tree produced by {@link AgentGrammarParser#spelerssituatie}.
	 * @param ctx the parse tree
	 */
	void enterSpelerssituatie(AgentGrammarParser.SpelerssituatieContext ctx);
	/**
	 * Exit a parse tree produced by {@link AgentGrammarParser#spelerssituatie}.
	 * @param ctx the parse tree
	 */
	void exitSpelerssituatie(AgentGrammarParser.SpelerssituatieContext ctx);
	/**
	 * Enter a parse tree produced by the {@code enOperatie}
	 * labeled alternative in {@link AgentGrammarParser#voegWoorden}.
	 * @param ctx the parse tree
	 */
	void enterEnOperatie(AgentGrammarParser.EnOperatieContext ctx);
	/**
	 * Exit a parse tree produced by the {@code enOperatie}
	 * labeled alternative in {@link AgentGrammarParser#voegWoorden}.
	 * @param ctx the parse tree
	 */
	void exitEnOperatie(AgentGrammarParser.EnOperatieContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ofOperatie}
	 * labeled alternative in {@link AgentGrammarParser#voegWoorden}.
	 * @param ctx the parse tree
	 */
	void enterOfOperatie(AgentGrammarParser.OfOperatieContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ofOperatie}
	 * labeled alternative in {@link AgentGrammarParser#voegWoorden}.
	 * @param ctx the parse tree
	 */
	void exitOfOperatie(AgentGrammarParser.OfOperatieContext ctx);
	/**
	 * Enter a parse tree produced by {@link AgentGrammarParser#kamerObservatie}.
	 * @param ctx the parse tree
	 */
	void enterKamerObservatie(AgentGrammarParser.KamerObservatieContext ctx);
	/**
	 * Exit a parse tree produced by {@link AgentGrammarParser#kamerObservatie}.
	 * @param ctx the parse tree
	 */
	void exitKamerObservatie(AgentGrammarParser.KamerObservatieContext ctx);
	/**
	 * Enter a parse tree produced by {@link AgentGrammarParser#gangObservatie}.
	 * @param ctx the parse tree
	 */
	void enterGangObservatie(AgentGrammarParser.GangObservatieContext ctx);
	/**
	 * Exit a parse tree produced by {@link AgentGrammarParser#gangObservatie}.
	 * @param ctx the parse tree
	 */
	void exitGangObservatie(AgentGrammarParser.GangObservatieContext ctx);
	/**
	 * Enter a parse tree produced by {@link AgentGrammarParser#actie}.
	 * @param ctx the parse tree
	 */
	void enterActie(AgentGrammarParser.ActieContext ctx);
	/**
	 * Exit a parse tree produced by {@link AgentGrammarParser#actie}.
	 * @param ctx the parse tree
	 */
	void exitActie(AgentGrammarParser.ActieContext ctx);
	/**
	 * Enter a parse tree produced by {@link AgentGrammarParser#loopActie}.
	 * @param ctx the parse tree
	 */
	void enterLoopActie(AgentGrammarParser.LoopActieContext ctx);
	/**
	 * Exit a parse tree produced by {@link AgentGrammarParser#loopActie}.
	 * @param ctx the parse tree
	 */
	void exitLoopActie(AgentGrammarParser.LoopActieContext ctx);
	/**
	 * Enter a parse tree produced by {@link AgentGrammarParser#aanvalsActie}.
	 * @param ctx the parse tree
	 */
	void enterAanvalsActie(AgentGrammarParser.AanvalsActieContext ctx);
	/**
	 * Exit a parse tree produced by {@link AgentGrammarParser#aanvalsActie}.
	 * @param ctx the parse tree
	 */
	void exitAanvalsActie(AgentGrammarParser.AanvalsActieContext ctx);
	/**
	 * Enter a parse tree produced by {@link AgentGrammarParser#interacteerActie}.
	 * @param ctx the parse tree
	 */
	void enterInteracteerActie(AgentGrammarParser.InteracteerActieContext ctx);
	/**
	 * Exit a parse tree produced by {@link AgentGrammarParser#interacteerActie}.
	 * @param ctx the parse tree
	 */
	void exitInteracteerActie(AgentGrammarParser.InteracteerActieContext ctx);
	/**
	 * Enter a parse tree produced by the {@code minderDanOperatie}
	 * labeled alternative in {@link AgentGrammarParser#operatie}.
	 * @param ctx the parse tree
	 */
	void enterMinderDanOperatie(AgentGrammarParser.MinderDanOperatieContext ctx);
	/**
	 * Exit a parse tree produced by the {@code minderDanOperatie}
	 * labeled alternative in {@link AgentGrammarParser#operatie}.
	 * @param ctx the parse tree
	 */
	void exitMinderDanOperatie(AgentGrammarParser.MinderDanOperatieContext ctx);
	/**
	 * Enter a parse tree produced by the {@code meerDanOperatie}
	 * labeled alternative in {@link AgentGrammarParser#operatie}.
	 * @param ctx the parse tree
	 */
	void enterMeerDanOperatie(AgentGrammarParser.MeerDanOperatieContext ctx);
	/**
	 * Exit a parse tree produced by the {@code meerDanOperatie}
	 * labeled alternative in {@link AgentGrammarParser#operatie}.
	 * @param ctx the parse tree
	 */
	void exitMeerDanOperatie(AgentGrammarParser.MeerDanOperatieContext ctx);
	/**
	 * Enter a parse tree produced by the {@code gelijkAanOperatie}
	 * labeled alternative in {@link AgentGrammarParser#operatie}.
	 * @param ctx the parse tree
	 */
	void enterGelijkAanOperatie(AgentGrammarParser.GelijkAanOperatieContext ctx);
	/**
	 * Exit a parse tree produced by the {@code gelijkAanOperatie}
	 * labeled alternative in {@link AgentGrammarParser#operatie}.
	 * @param ctx the parse tree
	 */
	void exitGelijkAanOperatie(AgentGrammarParser.GelijkAanOperatieContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nietGelijkAanOperatie}
	 * labeled alternative in {@link AgentGrammarParser#operatie}.
	 * @param ctx the parse tree
	 */
	void enterNietGelijkAanOperatie(AgentGrammarParser.NietGelijkAanOperatieContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nietGelijkAanOperatie}
	 * labeled alternative in {@link AgentGrammarParser#operatie}.
	 * @param ctx the parse tree
	 */
	void exitNietGelijkAanOperatie(AgentGrammarParser.NietGelijkAanOperatieContext ctx);
	/**
	 * Enter a parse tree produced by the {@code levenspuntenAttribuut}
	 * labeled alternative in {@link AgentGrammarParser#spelerAttribuut}.
	 * @param ctx the parse tree
	 */
	void enterLevenspuntenAttribuut(AgentGrammarParser.LevenspuntenAttribuutContext ctx);
	/**
	 * Exit a parse tree produced by the {@code levenspuntenAttribuut}
	 * labeled alternative in {@link AgentGrammarParser#spelerAttribuut}.
	 * @param ctx the parse tree
	 */
	void exitLevenspuntenAttribuut(AgentGrammarParser.LevenspuntenAttribuutContext ctx);
	/**
	 * Enter a parse tree produced by the {@code maximaleLevensAttribuut}
	 * labeled alternative in {@link AgentGrammarParser#spelerAttribuut}.
	 * @param ctx the parse tree
	 */
	void enterMaximaleLevensAttribuut(AgentGrammarParser.MaximaleLevensAttribuutContext ctx);
	/**
	 * Exit a parse tree produced by the {@code maximaleLevensAttribuut}
	 * labeled alternative in {@link AgentGrammarParser#spelerAttribuut}.
	 * @param ctx the parse tree
	 */
	void exitMaximaleLevensAttribuut(AgentGrammarParser.MaximaleLevensAttribuutContext ctx);
	/**
	 * Enter a parse tree produced by the {@code aanvalskrachtAttribuut}
	 * labeled alternative in {@link AgentGrammarParser#spelerAttribuut}.
	 * @param ctx the parse tree
	 */
	void enterAanvalskrachtAttribuut(AgentGrammarParser.AanvalskrachtAttribuutContext ctx);
	/**
	 * Exit a parse tree produced by the {@code aanvalskrachtAttribuut}
	 * labeled alternative in {@link AgentGrammarParser#spelerAttribuut}.
	 * @param ctx the parse tree
	 */
	void exitAanvalskrachtAttribuut(AgentGrammarParser.AanvalskrachtAttribuutContext ctx);
	/**
	 * Enter a parse tree produced by the {@code verdedigingskrachtAttribuut}
	 * labeled alternative in {@link AgentGrammarParser#spelerAttribuut}.
	 * @param ctx the parse tree
	 */
	void enterVerdedigingskrachtAttribuut(AgentGrammarParser.VerdedigingskrachtAttribuutContext ctx);
	/**
	 * Exit a parse tree produced by the {@code verdedigingskrachtAttribuut}
	 * labeled alternative in {@link AgentGrammarParser#spelerAttribuut}.
	 * @param ctx the parse tree
	 */
	void exitVerdedigingskrachtAttribuut(AgentGrammarParser.VerdedigingskrachtAttribuutContext ctx);
	/**
	 * Enter a parse tree produced by the {@code naarRichting}
	 * labeled alternative in {@link AgentGrammarParser#loopRichting}.
	 * @param ctx the parse tree
	 */
	void enterNaarRichting(AgentGrammarParser.NaarRichtingContext ctx);
	/**
	 * Exit a parse tree produced by the {@code naarRichting}
	 * labeled alternative in {@link AgentGrammarParser#loopRichting}.
	 * @param ctx the parse tree
	 */
	void exitNaarRichting(AgentGrammarParser.NaarRichtingContext ctx);
	/**
	 * Enter a parse tree produced by the {@code wegRichting}
	 * labeled alternative in {@link AgentGrammarParser#loopRichting}.
	 * @param ctx the parse tree
	 */
	void enterWegRichting(AgentGrammarParser.WegRichtingContext ctx);
	/**
	 * Exit a parse tree produced by the {@code wegRichting}
	 * labeled alternative in {@link AgentGrammarParser#loopRichting}.
	 * @param ctx the parse tree
	 */
	void exitWegRichting(AgentGrammarParser.WegRichtingContext ctx);
	/**
	 * Enter a parse tree produced by the {@code gangRichting}
	 * labeled alternative in {@link AgentGrammarParser#loopRichting}.
	 * @param ctx the parse tree
	 */
	void enterGangRichting(AgentGrammarParser.GangRichtingContext ctx);
	/**
	 * Exit a parse tree produced by the {@code gangRichting}
	 * labeled alternative in {@link AgentGrammarParser#loopRichting}.
	 * @param ctx the parse tree
	 */
	void exitGangRichting(AgentGrammarParser.GangRichtingContext ctx);
	/**
	 * Enter a parse tree produced by {@link AgentGrammarParser#nabijheidsLocatie}.
	 * @param ctx the parse tree
	 */
	void enterNabijheidsLocatie(AgentGrammarParser.NabijheidsLocatieContext ctx);
	/**
	 * Exit a parse tree produced by {@link AgentGrammarParser#nabijheidsLocatie}.
	 * @param ctx the parse tree
	 */
	void exitNabijheidsLocatie(AgentGrammarParser.NabijheidsLocatieContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dichtstbijzijnde}
	 * labeled alternative in {@link AgentGrammarParser#nabijheid}.
	 * @param ctx the parse tree
	 */
	void enterDichtstbijzijnde(AgentGrammarParser.DichtstbijzijndeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dichtstbijzijnde}
	 * labeled alternative in {@link AgentGrammarParser#nabijheid}.
	 * @param ctx the parse tree
	 */
	void exitDichtstbijzijnde(AgentGrammarParser.DichtstbijzijndeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code verste}
	 * labeled alternative in {@link AgentGrammarParser#nabijheid}.
	 * @param ctx the parse tree
	 */
	void enterVerste(AgentGrammarParser.VersteContext ctx);
	/**
	 * Exit a parse tree produced by the {@code verste}
	 * labeled alternative in {@link AgentGrammarParser#nabijheid}.
	 * @param ctx the parse tree
	 */
	void exitVerste(AgentGrammarParser.VersteContext ctx);
	/**
	 * Enter a parse tree produced by {@link AgentGrammarParser#integer}.
	 * @param ctx the parse tree
	 */
	void enterInteger(AgentGrammarParser.IntegerContext ctx);
	/**
	 * Exit a parse tree produced by {@link AgentGrammarParser#integer}.
	 * @param ctx the parse tree
	 */
	void exitInteger(AgentGrammarParser.IntegerContext ctx);
	/**
	 * Enter a parse tree produced by the {@code deur}
	 * labeled alternative in {@link AgentGrammarParser#omgevingsAttribuut}.
	 * @param ctx the parse tree
	 */
	void enterDeur(AgentGrammarParser.DeurContext ctx);
	/**
	 * Exit a parse tree produced by the {@code deur}
	 * labeled alternative in {@link AgentGrammarParser#omgevingsAttribuut}.
	 * @param ctx the parse tree
	 */
	void exitDeur(AgentGrammarParser.DeurContext ctx);
	/**
	 * Enter a parse tree produced by the {@code trap}
	 * labeled alternative in {@link AgentGrammarParser#omgevingsAttribuut}.
	 * @param ctx the parse tree
	 */
	void enterTrap(AgentGrammarParser.TrapContext ctx);
	/**
	 * Exit a parse tree produced by the {@code trap}
	 * labeled alternative in {@link AgentGrammarParser#omgevingsAttribuut}.
	 * @param ctx the parse tree
	 */
	void exitTrap(AgentGrammarParser.TrapContext ctx);
	/**
	 * Enter a parse tree produced by the {@code speler}
	 * labeled alternative in {@link AgentGrammarParser#omgevingsAttribuut}.
	 * @param ctx the parse tree
	 */
	void enterSpeler(AgentGrammarParser.SpelerContext ctx);
	/**
	 * Exit a parse tree produced by the {@code speler}
	 * labeled alternative in {@link AgentGrammarParser#omgevingsAttribuut}.
	 * @param ctx the parse tree
	 */
	void exitSpeler(AgentGrammarParser.SpelerContext ctx);
}