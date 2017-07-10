
import org.junit.runners.Suite;

import test_strutturale_interpreterPackage.Associator_test_case;
import test_strutturale_interpreterPackage.FinalParser_test_case;
import test_strutturale_interpreterPackage.PredicateParser_test_case;
import test_strutturale_interpreterPackage.TimeConstraints_test_case;
import test_strutturale_interpreterPackage.VerbPhraseParser_test_case;
import test_strutturale_mainPackage.Analyzer_test_case;

import org.junit.runner.RunWith;

@RunWith(Suite.class)
@Suite.SuiteClasses({VerbPhraseParser_test_case.class, 
	FinalParser_test_case.class, PredicateParser_test_case.class,
	Associator_test_case.class, TimeConstraints_test_case.class,
	Analyzer_test_case.class})
public class TestSuiteStatementCoverage {

}
