// Generated from /home/plusplus/Projects/asd-project-s2-2024-klas-b/src/main/java/com/han/towersofivory/agent/businesslayer/AgentGrammar.g4 by ANTLR 4.13.1
package com.han.towersofivory.agent.businesslayer.listeners;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class AgentGrammarParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		INT=1, WS=2, WANNEER=3, DAN=4, IK=5, DE=6, BEVAT=7, NAAR=8, EN=9, ZIJN=10, 
		KAMER=11, GANG=12, MIJN=13, MET=14, VAN=15, IN=16, BEN=17, EEN=18, OF=19, 
		IS=20, MINDERDAN=21, MEERDAN=22, NIETGELIJKAAN=23, GELIJKAAN=24, PUNT=25, 
		COMMA=26, SPATIE=27, LEVENSPUNTEN=28, MAXIMALELEVENS=29, AANVALSKRACHT=30, 
		VERDEDIGINGSKRACHT=31, DEUR=32, TRAP=33, SPELER=34, BOVEN=35, BENEDEN=36, 
		LINKS=37, RECHTS=38, TERUG=39, WEG=40, DOOR=41, LOOP=42, SLA=43, INTERACTEER=44, 
		DICHTSTBIJZIJNDE=45, VERSTE=46;
	public static final int
		RULE_agent = 0, RULE_regel = 1, RULE_expressie = 2, RULE_omgevingssituatie = 3, 
		RULE_spelerssituatie = 4, RULE_voegWoorden = 5, RULE_kamerObservatie = 6, 
		RULE_gangObservatie = 7, RULE_actie = 8, RULE_loopActie = 9, RULE_aanvalsActie = 10, 
		RULE_interacteerActie = 11, RULE_operatie = 12, RULE_spelerAttribuut = 13, 
		RULE_loopRichting = 14, RULE_nabijheidsLocatie = 15, RULE_nabijheid = 16, 
		RULE_integer = 17, RULE_omgevingsAttribuut = 18;
	private static String[] makeRuleNames() {
		return new String[] {
			"agent", "regel", "expressie", "omgevingssituatie", "spelerssituatie", 
			"voegWoorden", "kamerObservatie", "gangObservatie", "actie", "loopActie", 
			"aanvalsActie", "interacteerActie", "operatie", "spelerAttribuut", "loopRichting", 
			"nabijheidsLocatie", "nabijheid", "integer", "omgevingsAttribuut"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, "'Wanneer '", "'dan '", null, "'de '", "' bevat'", 
			"'naar '", "'en '", "'zijn'", "'kamer'", "'gang '", "'mijn '", "'met '", 
			"'van '", "'in '", "'ben'", "'een'", "'of '", "'is'", "'minder dan '", 
			"'meer dan '", null, null, "'.'", "', '", "' '", "'levenspunten'", "'maximale levenspunten'", 
			"'aanvalskracht'", "'verdedigingskracht'", null, null, null, "'boven'", 
			"'beneden'", "'links'", "'rechts'", "'terug'", "'weg '", "'door'", "'loop '", 
			"'sla '", "'interacteer '", "'dichtstbijzijnde '", "'verste '"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "INT", "WS", "WANNEER", "DAN", "IK", "DE", "BEVAT", "NAAR", "EN", 
			"ZIJN", "KAMER", "GANG", "MIJN", "MET", "VAN", "IN", "BEN", "EEN", "OF", 
			"IS", "MINDERDAN", "MEERDAN", "NIETGELIJKAAN", "GELIJKAAN", "PUNT", "COMMA", 
			"SPATIE", "LEVENSPUNTEN", "MAXIMALELEVENS", "AANVALSKRACHT", "VERDEDIGINGSKRACHT", 
			"DEUR", "TRAP", "SPELER", "BOVEN", "BENEDEN", "LINKS", "RECHTS", "TERUG", 
			"WEG", "DOOR", "LOOP", "SLA", "INTERACTEER", "DICHTSTBIJZIJNDE", "VERSTE"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "AgentGrammar.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public AgentGrammarParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AgentContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(AgentGrammarParser.EOF, 0); }
		public List<RegelContext> regel() {
			return getRuleContexts(RegelContext.class);
		}
		public RegelContext regel(int i) {
			return getRuleContext(RegelContext.class,i);
		}
		public AgentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_agent; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).enterAgent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).exitAgent(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AgentGrammarVisitor ) return ((AgentGrammarVisitor<? extends T>)visitor).visitAgent(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AgentContext agent() throws RecognitionException {
		AgentContext _localctx = new AgentContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_agent);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(39); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(38);
				regel();
				}
				}
				setState(41); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==WANNEER );
			setState(43);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RegelContext extends ParserRuleContext {
		public TerminalNode WANNEER() { return getToken(AgentGrammarParser.WANNEER, 0); }
		public ExpressieContext expressie() {
			return getRuleContext(ExpressieContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(AgentGrammarParser.COMMA, 0); }
		public ActieContext actie() {
			return getRuleContext(ActieContext.class,0);
		}
		public TerminalNode PUNT() { return getToken(AgentGrammarParser.PUNT, 0); }
		public RegelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_regel; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).enterRegel(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).exitRegel(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AgentGrammarVisitor ) return ((AgentGrammarVisitor<? extends T>)visitor).visitRegel(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RegelContext regel() throws RecognitionException {
		RegelContext _localctx = new RegelContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_regel);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(45);
			match(WANNEER);
			setState(46);
			expressie();
			setState(47);
			match(COMMA);
			setState(48);
			actie();
			setState(49);
			match(PUNT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressieContext extends ParserRuleContext {
		public List<SpelerssituatieContext> spelerssituatie() {
			return getRuleContexts(SpelerssituatieContext.class);
		}
		public SpelerssituatieContext spelerssituatie(int i) {
			return getRuleContext(SpelerssituatieContext.class,i);
		}
		public List<OmgevingssituatieContext> omgevingssituatie() {
			return getRuleContexts(OmgevingssituatieContext.class);
		}
		public OmgevingssituatieContext omgevingssituatie(int i) {
			return getRuleContext(OmgevingssituatieContext.class,i);
		}
		public List<VoegWoordenContext> voegWoorden() {
			return getRuleContexts(VoegWoordenContext.class);
		}
		public VoegWoordenContext voegWoorden(int i) {
			return getRuleContext(VoegWoordenContext.class,i);
		}
		public ExpressieContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expressie; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).enterExpressie(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).exitExpressie(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AgentGrammarVisitor ) return ((AgentGrammarVisitor<? extends T>)visitor).visitExpressie(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressieContext expressie() throws RecognitionException {
		ExpressieContext _localctx = new ExpressieContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_expressie);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(53);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case MIJN:
				{
				setState(51);
				spelerssituatie();
				}
				break;
			case IK:
			case DE:
				{
				setState(52);
				omgevingssituatie();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(62);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==EN || _la==OF) {
				{
				{
				setState(55);
				voegWoorden();
				setState(58);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case MIJN:
					{
					setState(56);
					spelerssituatie();
					}
					break;
				case IK:
				case DE:
					{
					setState(57);
					omgevingssituatie();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				}
				setState(64);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OmgevingssituatieContext extends ParserRuleContext {
		public KamerObservatieContext kamerObservatie() {
			return getRuleContext(KamerObservatieContext.class,0);
		}
		public GangObservatieContext gangObservatie() {
			return getRuleContext(GangObservatieContext.class,0);
		}
		public OmgevingssituatieContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_omgevingssituatie; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).enterOmgevingssituatie(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).exitOmgevingssituatie(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AgentGrammarVisitor ) return ((AgentGrammarVisitor<? extends T>)visitor).visitOmgevingssituatie(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OmgevingssituatieContext omgevingssituatie() throws RecognitionException {
		OmgevingssituatieContext _localctx = new OmgevingssituatieContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_omgevingssituatie);
		try {
			setState(67);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(65);
				kamerObservatie();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(66);
				gangObservatie();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SpelerssituatieContext extends ParserRuleContext {
		public TerminalNode MIJN() { return getToken(AgentGrammarParser.MIJN, 0); }
		public SpelerAttribuutContext spelerAttribuut() {
			return getRuleContext(SpelerAttribuutContext.class,0);
		}
		public OperatieContext operatie() {
			return getRuleContext(OperatieContext.class,0);
		}
		public IntegerContext integer() {
			return getRuleContext(IntegerContext.class,0);
		}
		public TerminalNode IS() { return getToken(AgentGrammarParser.IS, 0); }
		public TerminalNode ZIJN() { return getToken(AgentGrammarParser.ZIJN, 0); }
		public SpelerssituatieContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_spelerssituatie; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).enterSpelerssituatie(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).exitSpelerssituatie(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AgentGrammarVisitor ) return ((AgentGrammarVisitor<? extends T>)visitor).visitSpelerssituatie(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SpelerssituatieContext spelerssituatie() throws RecognitionException {
		SpelerssituatieContext _localctx = new SpelerssituatieContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_spelerssituatie);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(69);
			match(MIJN);
			setState(70);
			spelerAttribuut();
			setState(71);
			operatie();
			setState(72);
			integer();
			setState(73);
			_la = _input.LA(1);
			if ( !(_la==ZIJN || _la==IS) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VoegWoordenContext extends ParserRuleContext {
		public VoegWoordenContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_voegWoorden; }
	 
		public VoegWoordenContext() { }
		public void copyFrom(VoegWoordenContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class EnOperatieContext extends VoegWoordenContext {
		public TerminalNode EN() { return getToken(AgentGrammarParser.EN, 0); }
		public EnOperatieContext(VoegWoordenContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).enterEnOperatie(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).exitEnOperatie(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AgentGrammarVisitor ) return ((AgentGrammarVisitor<? extends T>)visitor).visitEnOperatie(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class OfOperatieContext extends VoegWoordenContext {
		public TerminalNode OF() { return getToken(AgentGrammarParser.OF, 0); }
		public OfOperatieContext(VoegWoordenContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).enterOfOperatie(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).exitOfOperatie(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AgentGrammarVisitor ) return ((AgentGrammarVisitor<? extends T>)visitor).visitOfOperatie(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VoegWoordenContext voegWoorden() throws RecognitionException {
		VoegWoordenContext _localctx = new VoegWoordenContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_voegWoorden);
		try {
			setState(77);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case EN:
				_localctx = new EnOperatieContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(75);
				match(EN);
				}
				break;
			case OF:
				_localctx = new OfOperatieContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(76);
				match(OF);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class KamerObservatieContext extends ParserRuleContext {
		public TerminalNode DE() { return getToken(AgentGrammarParser.DE, 0); }
		public TerminalNode KAMER() { return getToken(AgentGrammarParser.KAMER, 0); }
		public IntegerContext integer() {
			return getRuleContext(IntegerContext.class,0);
		}
		public OmgevingsAttribuutContext omgevingsAttribuut() {
			return getRuleContext(OmgevingsAttribuutContext.class,0);
		}
		public TerminalNode BEVAT() { return getToken(AgentGrammarParser.BEVAT, 0); }
		public OperatieContext operatie() {
			return getRuleContext(OperatieContext.class,0);
		}
		public KamerObservatieContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_kamerObservatie; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).enterKamerObservatie(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).exitKamerObservatie(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AgentGrammarVisitor ) return ((AgentGrammarVisitor<? extends T>)visitor).visitKamerObservatie(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KamerObservatieContext kamerObservatie() throws RecognitionException {
		KamerObservatieContext _localctx = new KamerObservatieContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_kamerObservatie);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(79);
			match(DE);
			setState(80);
			match(KAMER);
			setState(82);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 31457280L) != 0)) {
				{
				setState(81);
				operatie();
				}
			}

			setState(84);
			integer();
			setState(85);
			omgevingsAttribuut();
			setState(86);
			match(BEVAT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class GangObservatieContext extends ParserRuleContext {
		public TerminalNode DE() { return getToken(AgentGrammarParser.DE, 0); }
		public TerminalNode GANG() { return getToken(AgentGrammarParser.GANG, 0); }
		public IntegerContext integer() {
			return getRuleContext(IntegerContext.class,0);
		}
		public OmgevingsAttribuutContext omgevingsAttribuut() {
			return getRuleContext(OmgevingsAttribuutContext.class,0);
		}
		public TerminalNode BEVAT() { return getToken(AgentGrammarParser.BEVAT, 0); }
		public OperatieContext operatie() {
			return getRuleContext(OperatieContext.class,0);
		}
		public TerminalNode IK() { return getToken(AgentGrammarParser.IK, 0); }
		public TerminalNode IN() { return getToken(AgentGrammarParser.IN, 0); }
		public TerminalNode EEN() { return getToken(AgentGrammarParser.EEN, 0); }
		public TerminalNode BEN() { return getToken(AgentGrammarParser.BEN, 0); }
		public GangObservatieContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_gangObservatie; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).enterGangObservatie(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).exitGangObservatie(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AgentGrammarVisitor ) return ((AgentGrammarVisitor<? extends T>)visitor).visitGangObservatie(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GangObservatieContext gangObservatie() throws RecognitionException {
		GangObservatieContext _localctx = new GangObservatieContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_gangObservatie);
		int _la;
		try {
			setState(102);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case DE:
				enterOuterAlt(_localctx, 1);
				{
				setState(88);
				match(DE);
				setState(89);
				match(GANG);
				setState(91);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 31457280L) != 0)) {
					{
					setState(90);
					operatie();
					}
				}

				setState(93);
				integer();
				setState(94);
				omgevingsAttribuut();
				setState(95);
				match(BEVAT);
				}
				break;
			case IK:
				enterOuterAlt(_localctx, 2);
				{
				setState(97);
				match(IK);
				setState(98);
				match(IN);
				setState(99);
				match(EEN);
				setState(100);
				match(GANG);
				setState(101);
				match(BEN);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ActieContext extends ParserRuleContext {
		public TerminalNode DAN() { return getToken(AgentGrammarParser.DAN, 0); }
		public LoopActieContext loopActie() {
			return getRuleContext(LoopActieContext.class,0);
		}
		public AanvalsActieContext aanvalsActie() {
			return getRuleContext(AanvalsActieContext.class,0);
		}
		public InteracteerActieContext interacteerActie() {
			return getRuleContext(InteracteerActieContext.class,0);
		}
		public ActieContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_actie; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).enterActie(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).exitActie(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AgentGrammarVisitor ) return ((AgentGrammarVisitor<? extends T>)visitor).visitActie(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ActieContext actie() throws RecognitionException {
		ActieContext _localctx = new ActieContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_actie);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(104);
			match(DAN);
			setState(108);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LOOP:
				{
				setState(105);
				loopActie();
				}
				break;
			case SLA:
				{
				setState(106);
				aanvalsActie();
				}
				break;
			case INTERACTEER:
				{
				setState(107);
				interacteerActie();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LoopActieContext extends ParserRuleContext {
		public TerminalNode LOOP() { return getToken(AgentGrammarParser.LOOP, 0); }
		public TerminalNode IK() { return getToken(AgentGrammarParser.IK, 0); }
		public LoopRichtingContext loopRichting() {
			return getRuleContext(LoopRichtingContext.class,0);
		}
		public LoopActieContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_loopActie; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).enterLoopActie(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).exitLoopActie(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AgentGrammarVisitor ) return ((AgentGrammarVisitor<? extends T>)visitor).visitLoopActie(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LoopActieContext loopActie() throws RecognitionException {
		LoopActieContext _localctx = new LoopActieContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_loopActie);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(110);
			match(LOOP);
			setState(111);
			match(IK);
			setState(112);
			loopRichting();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AanvalsActieContext extends ParserRuleContext {
		public TerminalNode SLA() { return getToken(AgentGrammarParser.SLA, 0); }
		public TerminalNode IK() { return getToken(AgentGrammarParser.IK, 0); }
		public TerminalNode DE() { return getToken(AgentGrammarParser.DE, 0); }
		public NabijheidsLocatieContext nabijheidsLocatie() {
			return getRuleContext(NabijheidsLocatieContext.class,0);
		}
		public AanvalsActieContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_aanvalsActie; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).enterAanvalsActie(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).exitAanvalsActie(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AgentGrammarVisitor ) return ((AgentGrammarVisitor<? extends T>)visitor).visitAanvalsActie(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AanvalsActieContext aanvalsActie() throws RecognitionException {
		AanvalsActieContext _localctx = new AanvalsActieContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_aanvalsActie);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(114);
			match(SLA);
			setState(115);
			match(IK);
			setState(116);
			match(DE);
			setState(117);
			nabijheidsLocatie();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class InteracteerActieContext extends ParserRuleContext {
		public TerminalNode INTERACTEER() { return getToken(AgentGrammarParser.INTERACTEER, 0); }
		public TerminalNode IK() { return getToken(AgentGrammarParser.IK, 0); }
		public TerminalNode MET() { return getToken(AgentGrammarParser.MET, 0); }
		public TerminalNode DE() { return getToken(AgentGrammarParser.DE, 0); }
		public NabijheidsLocatieContext nabijheidsLocatie() {
			return getRuleContext(NabijheidsLocatieContext.class,0);
		}
		public InteracteerActieContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interacteerActie; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).enterInteracteerActie(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).exitInteracteerActie(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AgentGrammarVisitor ) return ((AgentGrammarVisitor<? extends T>)visitor).visitInteracteerActie(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InteracteerActieContext interacteerActie() throws RecognitionException {
		InteracteerActieContext _localctx = new InteracteerActieContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_interacteerActie);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(119);
			match(INTERACTEER);
			setState(120);
			match(IK);
			setState(121);
			match(MET);
			setState(122);
			match(DE);
			setState(123);
			nabijheidsLocatie();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OperatieContext extends ParserRuleContext {
		public OperatieContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operatie; }
	 
		public OperatieContext() { }
		public void copyFrom(OperatieContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class MeerDanOperatieContext extends OperatieContext {
		public TerminalNode MEERDAN() { return getToken(AgentGrammarParser.MEERDAN, 0); }
		public MeerDanOperatieContext(OperatieContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).enterMeerDanOperatie(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).exitMeerDanOperatie(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AgentGrammarVisitor ) return ((AgentGrammarVisitor<? extends T>)visitor).visitMeerDanOperatie(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class GelijkAanOperatieContext extends OperatieContext {
		public TerminalNode GELIJKAAN() { return getToken(AgentGrammarParser.GELIJKAAN, 0); }
		public GelijkAanOperatieContext(OperatieContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).enterGelijkAanOperatie(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).exitGelijkAanOperatie(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AgentGrammarVisitor ) return ((AgentGrammarVisitor<? extends T>)visitor).visitGelijkAanOperatie(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class MinderDanOperatieContext extends OperatieContext {
		public TerminalNode MINDERDAN() { return getToken(AgentGrammarParser.MINDERDAN, 0); }
		public MinderDanOperatieContext(OperatieContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).enterMinderDanOperatie(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).exitMinderDanOperatie(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AgentGrammarVisitor ) return ((AgentGrammarVisitor<? extends T>)visitor).visitMinderDanOperatie(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NietGelijkAanOperatieContext extends OperatieContext {
		public TerminalNode NIETGELIJKAAN() { return getToken(AgentGrammarParser.NIETGELIJKAAN, 0); }
		public NietGelijkAanOperatieContext(OperatieContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).enterNietGelijkAanOperatie(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).exitNietGelijkAanOperatie(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AgentGrammarVisitor ) return ((AgentGrammarVisitor<? extends T>)visitor).visitNietGelijkAanOperatie(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OperatieContext operatie() throws RecognitionException {
		OperatieContext _localctx = new OperatieContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_operatie);
		try {
			setState(129);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case MINDERDAN:
				_localctx = new MinderDanOperatieContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(125);
				match(MINDERDAN);
				}
				break;
			case MEERDAN:
				_localctx = new MeerDanOperatieContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(126);
				match(MEERDAN);
				}
				break;
			case GELIJKAAN:
				_localctx = new GelijkAanOperatieContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(127);
				match(GELIJKAAN);
				}
				break;
			case NIETGELIJKAAN:
				_localctx = new NietGelijkAanOperatieContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(128);
				match(NIETGELIJKAAN);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SpelerAttribuutContext extends ParserRuleContext {
		public SpelerAttribuutContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_spelerAttribuut; }
	 
		public SpelerAttribuutContext() { }
		public void copyFrom(SpelerAttribuutContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AanvalskrachtAttribuutContext extends SpelerAttribuutContext {
		public TerminalNode AANVALSKRACHT() { return getToken(AgentGrammarParser.AANVALSKRACHT, 0); }
		public AanvalskrachtAttribuutContext(SpelerAttribuutContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).enterAanvalskrachtAttribuut(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).exitAanvalskrachtAttribuut(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AgentGrammarVisitor ) return ((AgentGrammarVisitor<? extends T>)visitor).visitAanvalskrachtAttribuut(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class MaximaleLevensAttribuutContext extends SpelerAttribuutContext {
		public TerminalNode MAXIMALELEVENS() { return getToken(AgentGrammarParser.MAXIMALELEVENS, 0); }
		public MaximaleLevensAttribuutContext(SpelerAttribuutContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).enterMaximaleLevensAttribuut(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).exitMaximaleLevensAttribuut(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AgentGrammarVisitor ) return ((AgentGrammarVisitor<? extends T>)visitor).visitMaximaleLevensAttribuut(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class VerdedigingskrachtAttribuutContext extends SpelerAttribuutContext {
		public TerminalNode VERDEDIGINGSKRACHT() { return getToken(AgentGrammarParser.VERDEDIGINGSKRACHT, 0); }
		public VerdedigingskrachtAttribuutContext(SpelerAttribuutContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).enterVerdedigingskrachtAttribuut(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).exitVerdedigingskrachtAttribuut(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AgentGrammarVisitor ) return ((AgentGrammarVisitor<? extends T>)visitor).visitVerdedigingskrachtAttribuut(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class LevenspuntenAttribuutContext extends SpelerAttribuutContext {
		public TerminalNode LEVENSPUNTEN() { return getToken(AgentGrammarParser.LEVENSPUNTEN, 0); }
		public LevenspuntenAttribuutContext(SpelerAttribuutContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).enterLevenspuntenAttribuut(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).exitLevenspuntenAttribuut(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AgentGrammarVisitor ) return ((AgentGrammarVisitor<? extends T>)visitor).visitLevenspuntenAttribuut(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SpelerAttribuutContext spelerAttribuut() throws RecognitionException {
		SpelerAttribuutContext _localctx = new SpelerAttribuutContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_spelerAttribuut);
		try {
			setState(135);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LEVENSPUNTEN:
				_localctx = new LevenspuntenAttribuutContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(131);
				match(LEVENSPUNTEN);
				}
				break;
			case MAXIMALELEVENS:
				_localctx = new MaximaleLevensAttribuutContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(132);
				match(MAXIMALELEVENS);
				}
				break;
			case AANVALSKRACHT:
				_localctx = new AanvalskrachtAttribuutContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(133);
				match(AANVALSKRACHT);
				}
				break;
			case VERDEDIGINGSKRACHT:
				_localctx = new VerdedigingskrachtAttribuutContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(134);
				match(VERDEDIGINGSKRACHT);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LoopRichtingContext extends ParserRuleContext {
		public LoopRichtingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_loopRichting; }
	 
		public LoopRichtingContext() { }
		public void copyFrom(LoopRichtingContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NaarRichtingContext extends LoopRichtingContext {
		public TerminalNode NAAR() { return getToken(AgentGrammarParser.NAAR, 0); }
		public TerminalNode DE() { return getToken(AgentGrammarParser.DE, 0); }
		public NabijheidsLocatieContext nabijheidsLocatie() {
			return getRuleContext(NabijheidsLocatieContext.class,0);
		}
		public NaarRichtingContext(LoopRichtingContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).enterNaarRichting(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).exitNaarRichting(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AgentGrammarVisitor ) return ((AgentGrammarVisitor<? extends T>)visitor).visitNaarRichting(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class GangRichtingContext extends LoopRichtingContext {
		public TerminalNode DE() { return getToken(AgentGrammarParser.DE, 0); }
		public TerminalNode GANG() { return getToken(AgentGrammarParser.GANG, 0); }
		public TerminalNode DOOR() { return getToken(AgentGrammarParser.DOOR, 0); }
		public GangRichtingContext(LoopRichtingContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).enterGangRichting(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).exitGangRichting(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AgentGrammarVisitor ) return ((AgentGrammarVisitor<? extends T>)visitor).visitGangRichting(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class WegRichtingContext extends LoopRichtingContext {
		public TerminalNode WEG() { return getToken(AgentGrammarParser.WEG, 0); }
		public TerminalNode VAN() { return getToken(AgentGrammarParser.VAN, 0); }
		public TerminalNode DE() { return getToken(AgentGrammarParser.DE, 0); }
		public NabijheidsLocatieContext nabijheidsLocatie() {
			return getRuleContext(NabijheidsLocatieContext.class,0);
		}
		public WegRichtingContext(LoopRichtingContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).enterWegRichting(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).exitWegRichting(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AgentGrammarVisitor ) return ((AgentGrammarVisitor<? extends T>)visitor).visitWegRichting(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LoopRichtingContext loopRichting() throws RecognitionException {
		LoopRichtingContext _localctx = new LoopRichtingContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_loopRichting);
		try {
			setState(147);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NAAR:
				_localctx = new NaarRichtingContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(137);
				match(NAAR);
				setState(138);
				match(DE);
				setState(139);
				nabijheidsLocatie();
				}
				}
				break;
			case WEG:
				_localctx = new WegRichtingContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(140);
				match(WEG);
				setState(141);
				match(VAN);
				setState(142);
				match(DE);
				setState(143);
				nabijheidsLocatie();
				}
				}
				break;
			case DE:
				_localctx = new GangRichtingContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(144);
				match(DE);
				setState(145);
				match(GANG);
				setState(146);
				match(DOOR);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NabijheidsLocatieContext extends ParserRuleContext {
		public OmgevingsAttribuutContext omgevingsAttribuut() {
			return getRuleContext(OmgevingsAttribuutContext.class,0);
		}
		public NabijheidContext nabijheid() {
			return getRuleContext(NabijheidContext.class,0);
		}
		public NabijheidsLocatieContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nabijheidsLocatie; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).enterNabijheidsLocatie(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).exitNabijheidsLocatie(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AgentGrammarVisitor ) return ((AgentGrammarVisitor<? extends T>)visitor).visitNabijheidsLocatie(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NabijheidsLocatieContext nabijheidsLocatie() throws RecognitionException {
		NabijheidsLocatieContext _localctx = new NabijheidsLocatieContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_nabijheidsLocatie);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(150);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DICHTSTBIJZIJNDE || _la==VERSTE) {
				{
				setState(149);
				nabijheid();
				}
			}

			setState(152);
			omgevingsAttribuut();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NabijheidContext extends ParserRuleContext {
		public NabijheidContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nabijheid; }
	 
		public NabijheidContext() { }
		public void copyFrom(NabijheidContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class VersteContext extends NabijheidContext {
		public TerminalNode VERSTE() { return getToken(AgentGrammarParser.VERSTE, 0); }
		public VersteContext(NabijheidContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).enterVerste(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).exitVerste(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AgentGrammarVisitor ) return ((AgentGrammarVisitor<? extends T>)visitor).visitVerste(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class DichtstbijzijndeContext extends NabijheidContext {
		public TerminalNode DICHTSTBIJZIJNDE() { return getToken(AgentGrammarParser.DICHTSTBIJZIJNDE, 0); }
		public DichtstbijzijndeContext(NabijheidContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).enterDichtstbijzijnde(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).exitDichtstbijzijnde(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AgentGrammarVisitor ) return ((AgentGrammarVisitor<? extends T>)visitor).visitDichtstbijzijnde(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NabijheidContext nabijheid() throws RecognitionException {
		NabijheidContext _localctx = new NabijheidContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_nabijheid);
		try {
			setState(156);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case DICHTSTBIJZIJNDE:
				_localctx = new DichtstbijzijndeContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(154);
				match(DICHTSTBIJZIJNDE);
				}
				break;
			case VERSTE:
				_localctx = new VersteContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(155);
				match(VERSTE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IntegerContext extends ParserRuleContext {
		public TerminalNode INT() { return getToken(AgentGrammarParser.INT, 0); }
		public IntegerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_integer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).enterInteger(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).exitInteger(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AgentGrammarVisitor ) return ((AgentGrammarVisitor<? extends T>)visitor).visitInteger(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IntegerContext integer() throws RecognitionException {
		IntegerContext _localctx = new IntegerContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_integer);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(158);
			match(INT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OmgevingsAttribuutContext extends ParserRuleContext {
		public OmgevingsAttribuutContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_omgevingsAttribuut; }
	 
		public OmgevingsAttribuutContext() { }
		public void copyFrom(OmgevingsAttribuutContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class DeurContext extends OmgevingsAttribuutContext {
		public TerminalNode DEUR() { return getToken(AgentGrammarParser.DEUR, 0); }
		public DeurContext(OmgevingsAttribuutContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).enterDeur(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).exitDeur(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AgentGrammarVisitor ) return ((AgentGrammarVisitor<? extends T>)visitor).visitDeur(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class SpelerContext extends OmgevingsAttribuutContext {
		public TerminalNode SPELER() { return getToken(AgentGrammarParser.SPELER, 0); }
		public SpelerContext(OmgevingsAttribuutContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).enterSpeler(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).exitSpeler(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AgentGrammarVisitor ) return ((AgentGrammarVisitor<? extends T>)visitor).visitSpeler(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class TrapContext extends OmgevingsAttribuutContext {
		public TerminalNode TRAP() { return getToken(AgentGrammarParser.TRAP, 0); }
		public TrapContext(OmgevingsAttribuutContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).enterTrap(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AgentGrammarListener ) ((AgentGrammarListener)listener).exitTrap(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AgentGrammarVisitor ) return ((AgentGrammarVisitor<? extends T>)visitor).visitTrap(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OmgevingsAttribuutContext omgevingsAttribuut() throws RecognitionException {
		OmgevingsAttribuutContext _localctx = new OmgevingsAttribuutContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_omgevingsAttribuut);
		try {
			setState(163);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case DEUR:
				_localctx = new DeurContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(160);
				match(DEUR);
				}
				break;
			case TRAP:
				_localctx = new TrapContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(161);
				match(TRAP);
				}
				break;
			case SPELER:
				_localctx = new SpelerContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(162);
				match(SPELER);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u0001.\u00a6\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0001\u0000\u0004\u0000(\b\u0000\u000b\u0000\f\u0000)\u0001\u0000\u0001"+
		"\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0002\u0001\u0002\u0003\u00026\b\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0003\u0002;\b\u0002\u0005\u0002=\b\u0002\n\u0002\f"+
		"\u0002@\t\u0002\u0001\u0003\u0001\u0003\u0003\u0003D\b\u0003\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0005"+
		"\u0001\u0005\u0003\u0005N\b\u0005\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0003\u0006S\b\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0003\u0007\\\b\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0003\u0007g\b\u0007\u0001\b\u0001\b\u0001\b"+
		"\u0001\b\u0003\bm\b\b\u0001\t\u0001\t\u0001\t\u0001\t\u0001\n\u0001\n"+
		"\u0001\n\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b"+
		"\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0003\f\u0082"+
		"\b\f\u0001\r\u0001\r\u0001\r\u0001\r\u0003\r\u0088\b\r\u0001\u000e\u0001"+
		"\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001"+
		"\u000e\u0001\u000e\u0001\u000e\u0003\u000e\u0094\b\u000e\u0001\u000f\u0003"+
		"\u000f\u0097\b\u000f\u0001\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0003"+
		"\u0010\u009d\b\u0010\u0001\u0011\u0001\u0011\u0001\u0012\u0001\u0012\u0001"+
		"\u0012\u0003\u0012\u00a4\b\u0012\u0001\u0012\u0000\u0000\u0013\u0000\u0002"+
		"\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e"+
		" \"$\u0000\u0001\u0002\u0000\n\n\u0014\u0014\u00a9\u0000\'\u0001\u0000"+
		"\u0000\u0000\u0002-\u0001\u0000\u0000\u0000\u00045\u0001\u0000\u0000\u0000"+
		"\u0006C\u0001\u0000\u0000\u0000\bE\u0001\u0000\u0000\u0000\nM\u0001\u0000"+
		"\u0000\u0000\fO\u0001\u0000\u0000\u0000\u000ef\u0001\u0000\u0000\u0000"+
		"\u0010h\u0001\u0000\u0000\u0000\u0012n\u0001\u0000\u0000\u0000\u0014r"+
		"\u0001\u0000\u0000\u0000\u0016w\u0001\u0000\u0000\u0000\u0018\u0081\u0001"+
		"\u0000\u0000\u0000\u001a\u0087\u0001\u0000\u0000\u0000\u001c\u0093\u0001"+
		"\u0000\u0000\u0000\u001e\u0096\u0001\u0000\u0000\u0000 \u009c\u0001\u0000"+
		"\u0000\u0000\"\u009e\u0001\u0000\u0000\u0000$\u00a3\u0001\u0000\u0000"+
		"\u0000&(\u0003\u0002\u0001\u0000\'&\u0001\u0000\u0000\u0000()\u0001\u0000"+
		"\u0000\u0000)\'\u0001\u0000\u0000\u0000)*\u0001\u0000\u0000\u0000*+\u0001"+
		"\u0000\u0000\u0000+,\u0005\u0000\u0000\u0001,\u0001\u0001\u0000\u0000"+
		"\u0000-.\u0005\u0003\u0000\u0000./\u0003\u0004\u0002\u0000/0\u0005\u001a"+
		"\u0000\u000001\u0003\u0010\b\u000012\u0005\u0019\u0000\u00002\u0003\u0001"+
		"\u0000\u0000\u000036\u0003\b\u0004\u000046\u0003\u0006\u0003\u000053\u0001"+
		"\u0000\u0000\u000054\u0001\u0000\u0000\u00006>\u0001\u0000\u0000\u0000"+
		"7:\u0003\n\u0005\u00008;\u0003\b\u0004\u00009;\u0003\u0006\u0003\u0000"+
		":8\u0001\u0000\u0000\u0000:9\u0001\u0000\u0000\u0000;=\u0001\u0000\u0000"+
		"\u0000<7\u0001\u0000\u0000\u0000=@\u0001\u0000\u0000\u0000><\u0001\u0000"+
		"\u0000\u0000>?\u0001\u0000\u0000\u0000?\u0005\u0001\u0000\u0000\u0000"+
		"@>\u0001\u0000\u0000\u0000AD\u0003\f\u0006\u0000BD\u0003\u000e\u0007\u0000"+
		"CA\u0001\u0000\u0000\u0000CB\u0001\u0000\u0000\u0000D\u0007\u0001\u0000"+
		"\u0000\u0000EF\u0005\r\u0000\u0000FG\u0003\u001a\r\u0000GH\u0003\u0018"+
		"\f\u0000HI\u0003\"\u0011\u0000IJ\u0007\u0000\u0000\u0000J\t\u0001\u0000"+
		"\u0000\u0000KN\u0005\t\u0000\u0000LN\u0005\u0013\u0000\u0000MK\u0001\u0000"+
		"\u0000\u0000ML\u0001\u0000\u0000\u0000N\u000b\u0001\u0000\u0000\u0000"+
		"OP\u0005\u0006\u0000\u0000PR\u0005\u000b\u0000\u0000QS\u0003\u0018\f\u0000"+
		"RQ\u0001\u0000\u0000\u0000RS\u0001\u0000\u0000\u0000ST\u0001\u0000\u0000"+
		"\u0000TU\u0003\"\u0011\u0000UV\u0003$\u0012\u0000VW\u0005\u0007\u0000"+
		"\u0000W\r\u0001\u0000\u0000\u0000XY\u0005\u0006\u0000\u0000Y[\u0005\f"+
		"\u0000\u0000Z\\\u0003\u0018\f\u0000[Z\u0001\u0000\u0000\u0000[\\\u0001"+
		"\u0000\u0000\u0000\\]\u0001\u0000\u0000\u0000]^\u0003\"\u0011\u0000^_"+
		"\u0003$\u0012\u0000_`\u0005\u0007\u0000\u0000`g\u0001\u0000\u0000\u0000"+
		"ab\u0005\u0005\u0000\u0000bc\u0005\u0010\u0000\u0000cd\u0005\u0012\u0000"+
		"\u0000de\u0005\f\u0000\u0000eg\u0005\u0011\u0000\u0000fX\u0001\u0000\u0000"+
		"\u0000fa\u0001\u0000\u0000\u0000g\u000f\u0001\u0000\u0000\u0000hl\u0005"+
		"\u0004\u0000\u0000im\u0003\u0012\t\u0000jm\u0003\u0014\n\u0000km\u0003"+
		"\u0016\u000b\u0000li\u0001\u0000\u0000\u0000lj\u0001\u0000\u0000\u0000"+
		"lk\u0001\u0000\u0000\u0000m\u0011\u0001\u0000\u0000\u0000no\u0005*\u0000"+
		"\u0000op\u0005\u0005\u0000\u0000pq\u0003\u001c\u000e\u0000q\u0013\u0001"+
		"\u0000\u0000\u0000rs\u0005+\u0000\u0000st\u0005\u0005\u0000\u0000tu\u0005"+
		"\u0006\u0000\u0000uv\u0003\u001e\u000f\u0000v\u0015\u0001\u0000\u0000"+
		"\u0000wx\u0005,\u0000\u0000xy\u0005\u0005\u0000\u0000yz\u0005\u000e\u0000"+
		"\u0000z{\u0005\u0006\u0000\u0000{|\u0003\u001e\u000f\u0000|\u0017\u0001"+
		"\u0000\u0000\u0000}\u0082\u0005\u0015\u0000\u0000~\u0082\u0005\u0016\u0000"+
		"\u0000\u007f\u0082\u0005\u0018\u0000\u0000\u0080\u0082\u0005\u0017\u0000"+
		"\u0000\u0081}\u0001\u0000\u0000\u0000\u0081~\u0001\u0000\u0000\u0000\u0081"+
		"\u007f\u0001\u0000\u0000\u0000\u0081\u0080\u0001\u0000\u0000\u0000\u0082"+
		"\u0019\u0001\u0000\u0000\u0000\u0083\u0088\u0005\u001c\u0000\u0000\u0084"+
		"\u0088\u0005\u001d\u0000\u0000\u0085\u0088\u0005\u001e\u0000\u0000\u0086"+
		"\u0088\u0005\u001f\u0000\u0000\u0087\u0083\u0001\u0000\u0000\u0000\u0087"+
		"\u0084\u0001\u0000\u0000\u0000\u0087\u0085\u0001\u0000\u0000\u0000\u0087"+
		"\u0086\u0001\u0000\u0000\u0000\u0088\u001b\u0001\u0000\u0000\u0000\u0089"+
		"\u008a\u0005\b\u0000\u0000\u008a\u008b\u0005\u0006\u0000\u0000\u008b\u0094"+
		"\u0003\u001e\u000f\u0000\u008c\u008d\u0005(\u0000\u0000\u008d\u008e\u0005"+
		"\u000f\u0000\u0000\u008e\u008f\u0005\u0006\u0000\u0000\u008f\u0094\u0003"+
		"\u001e\u000f\u0000\u0090\u0091\u0005\u0006\u0000\u0000\u0091\u0092\u0005"+
		"\f\u0000\u0000\u0092\u0094\u0005)\u0000\u0000\u0093\u0089\u0001\u0000"+
		"\u0000\u0000\u0093\u008c\u0001\u0000\u0000\u0000\u0093\u0090\u0001\u0000"+
		"\u0000\u0000\u0094\u001d\u0001\u0000\u0000\u0000\u0095\u0097\u0003 \u0010"+
		"\u0000\u0096\u0095\u0001\u0000\u0000\u0000\u0096\u0097\u0001\u0000\u0000"+
		"\u0000\u0097\u0098\u0001\u0000\u0000\u0000\u0098\u0099\u0003$\u0012\u0000"+
		"\u0099\u001f\u0001\u0000\u0000\u0000\u009a\u009d\u0005-\u0000\u0000\u009b"+
		"\u009d\u0005.\u0000\u0000\u009c\u009a\u0001\u0000\u0000\u0000\u009c\u009b"+
		"\u0001\u0000\u0000\u0000\u009d!\u0001\u0000\u0000\u0000\u009e\u009f\u0005"+
		"\u0001\u0000\u0000\u009f#\u0001\u0000\u0000\u0000\u00a0\u00a4\u0005 \u0000"+
		"\u0000\u00a1\u00a4\u0005!\u0000\u0000\u00a2\u00a4\u0005\"\u0000\u0000"+
		"\u00a3\u00a0\u0001\u0000\u0000\u0000\u00a3\u00a1\u0001\u0000\u0000\u0000"+
		"\u00a3\u00a2\u0001\u0000\u0000\u0000\u00a4%\u0001\u0000\u0000\u0000\u0010"+
		")5:>CMR[fl\u0081\u0087\u0093\u0096\u009c\u00a3";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}