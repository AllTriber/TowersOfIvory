// Generated from /home/plusplus/Projects/asd-project-s2-2024-klas-b/src/main/java/com/han/towersofivory/agent/businesslayer/AgentGrammar.g4 by ANTLR 4.13.1
package com.han.towersofivory.agent.businesslayer.listeners;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link AgentGrammarParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface AgentGrammarVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link AgentGrammarParser#agent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAgent(AgentGrammarParser.AgentContext ctx);
	/**
	 * Visit a parse tree produced by {@link AgentGrammarParser#regel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRegel(AgentGrammarParser.RegelContext ctx);
	/**
	 * Visit a parse tree produced by {@link AgentGrammarParser#expressie}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressie(AgentGrammarParser.ExpressieContext ctx);
	/**
	 * Visit a parse tree produced by {@link AgentGrammarParser#omgevingssituatie}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOmgevingssituatie(AgentGrammarParser.OmgevingssituatieContext ctx);
	/**
	 * Visit a parse tree produced by {@link AgentGrammarParser#spelerssituatie}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpelerssituatie(AgentGrammarParser.SpelerssituatieContext ctx);
	/**
	 * Visit a parse tree produced by the {@code enOperatie}
	 * labeled alternative in {@link AgentGrammarParser#voegWoorden}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnOperatie(AgentGrammarParser.EnOperatieContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ofOperatie}
	 * labeled alternative in {@link AgentGrammarParser#voegWoorden}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOfOperatie(AgentGrammarParser.OfOperatieContext ctx);
	/**
	 * Visit a parse tree produced by {@link AgentGrammarParser#kamerObservatie}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKamerObservatie(AgentGrammarParser.KamerObservatieContext ctx);
	/**
	 * Visit a parse tree produced by {@link AgentGrammarParser#gangObservatie}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGangObservatie(AgentGrammarParser.GangObservatieContext ctx);
	/**
	 * Visit a parse tree produced by {@link AgentGrammarParser#actie}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActie(AgentGrammarParser.ActieContext ctx);
	/**
	 * Visit a parse tree produced by {@link AgentGrammarParser#loopActie}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoopActie(AgentGrammarParser.LoopActieContext ctx);
	/**
	 * Visit a parse tree produced by {@link AgentGrammarParser#aanvalsActie}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAanvalsActie(AgentGrammarParser.AanvalsActieContext ctx);
	/**
	 * Visit a parse tree produced by {@link AgentGrammarParser#interacteerActie}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInteracteerActie(AgentGrammarParser.InteracteerActieContext ctx);
	/**
	 * Visit a parse tree produced by the {@code minderDanOperatie}
	 * labeled alternative in {@link AgentGrammarParser#operatie}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMinderDanOperatie(AgentGrammarParser.MinderDanOperatieContext ctx);
	/**
	 * Visit a parse tree produced by the {@code meerDanOperatie}
	 * labeled alternative in {@link AgentGrammarParser#operatie}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMeerDanOperatie(AgentGrammarParser.MeerDanOperatieContext ctx);
	/**
	 * Visit a parse tree produced by the {@code gelijkAanOperatie}
	 * labeled alternative in {@link AgentGrammarParser#operatie}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGelijkAanOperatie(AgentGrammarParser.GelijkAanOperatieContext ctx);
	/**
	 * Visit a parse tree produced by the {@code nietGelijkAanOperatie}
	 * labeled alternative in {@link AgentGrammarParser#operatie}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNietGelijkAanOperatie(AgentGrammarParser.NietGelijkAanOperatieContext ctx);
	/**
	 * Visit a parse tree produced by the {@code levenspuntenAttribuut}
	 * labeled alternative in {@link AgentGrammarParser#spelerAttribuut}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLevenspuntenAttribuut(AgentGrammarParser.LevenspuntenAttribuutContext ctx);
	/**
	 * Visit a parse tree produced by the {@code maximaleLevensAttribuut}
	 * labeled alternative in {@link AgentGrammarParser#spelerAttribuut}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMaximaleLevensAttribuut(AgentGrammarParser.MaximaleLevensAttribuutContext ctx);
	/**
	 * Visit a parse tree produced by the {@code aanvalskrachtAttribuut}
	 * labeled alternative in {@link AgentGrammarParser#spelerAttribuut}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAanvalskrachtAttribuut(AgentGrammarParser.AanvalskrachtAttribuutContext ctx);
	/**
	 * Visit a parse tree produced by the {@code verdedigingskrachtAttribuut}
	 * labeled alternative in {@link AgentGrammarParser#spelerAttribuut}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVerdedigingskrachtAttribuut(AgentGrammarParser.VerdedigingskrachtAttribuutContext ctx);
	/**
	 * Visit a parse tree produced by the {@code naarRichting}
	 * labeled alternative in {@link AgentGrammarParser#loopRichting}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNaarRichting(AgentGrammarParser.NaarRichtingContext ctx);
	/**
	 * Visit a parse tree produced by the {@code wegRichting}
	 * labeled alternative in {@link AgentGrammarParser#loopRichting}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWegRichting(AgentGrammarParser.WegRichtingContext ctx);
	/**
	 * Visit a parse tree produced by the {@code gangRichting}
	 * labeled alternative in {@link AgentGrammarParser#loopRichting}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGangRichting(AgentGrammarParser.GangRichtingContext ctx);
	/**
	 * Visit a parse tree produced by {@link AgentGrammarParser#nabijheidsLocatie}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNabijheidsLocatie(AgentGrammarParser.NabijheidsLocatieContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dichtstbijzijnde}
	 * labeled alternative in {@link AgentGrammarParser#nabijheid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDichtstbijzijnde(AgentGrammarParser.DichtstbijzijndeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code verste}
	 * labeled alternative in {@link AgentGrammarParser#nabijheid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVerste(AgentGrammarParser.VersteContext ctx);
	/**
	 * Visit a parse tree produced by {@link AgentGrammarParser#integer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInteger(AgentGrammarParser.IntegerContext ctx);
	/**
	 * Visit a parse tree produced by the {@code deur}
	 * labeled alternative in {@link AgentGrammarParser#omgevingsAttribuut}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeur(AgentGrammarParser.DeurContext ctx);
	/**
	 * Visit a parse tree produced by the {@code trap}
	 * labeled alternative in {@link AgentGrammarParser#omgevingsAttribuut}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrap(AgentGrammarParser.TrapContext ctx);
	/**
	 * Visit a parse tree produced by the {@code speler}
	 * labeled alternative in {@link AgentGrammarParser#omgevingsAttribuut}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpeler(AgentGrammarParser.SpelerContext ctx);
}