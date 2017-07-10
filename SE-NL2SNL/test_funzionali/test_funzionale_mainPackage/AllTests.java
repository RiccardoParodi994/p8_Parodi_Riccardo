package test_funzionale_mainPackage;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test_funzionale_UC1.ScenarioAlternativo2a_test_case;
import test_funzionale_UC1.ScenarioPrincipale_test_case;

/**
 * La percentuale di statement coverage raggiunta 
 * eseguendo questa test suite rappresentante il test funzionale
 * risulta essere : 64,2%
 * 
 * La percentuale di statement coverage raggiunta 
 * eseguendo questa test suite rappresentante il test funzionale
 * risulta essere : 58,1%
 * 
 * @author Riccardo Parodi
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ScenarioPrincipale_test_case.class,
	ScenarioAlternativo2a_test_case.class})
public class AllTests {

}
