package es.usc.citius.composit.test.composition;

import com.google.common.base.Stopwatch;
import es.usc.citius.composit.core.composition.DiscoveryIO;
import es.usc.citius.composit.core.composition.MatchBasedDiscoveryIO;
import es.usc.citius.composit.core.composition.network.ServiceMatchNetwork;
import es.usc.citius.composit.core.composition.search.ForwardServiceDiscoverer;
import es.usc.citius.composit.core.knowledge.Concept;
import es.usc.citius.composit.core.provider.MemoryIndexServiceProvider;
import es.usc.citius.composit.core.provider.ServiceProvider;
import es.usc.citius.composit.wsc08.data.WSCTest;
import es.usc.citius.composit.wsc08.data.matcher.WSCKBMatchGraph;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class ForwardServiceDiscovererWSC {
    private static final Logger log = LoggerFactory.getLogger(ForwardServiceDiscovererWSC.class);

    public static ServiceMatchNetwork<Concept, Boolean> generateGraph(WSCTest.Dataset dataset) throws IOException {
        // Use an indexed service provider to optimize the graph generation phase
        ServiceProvider<Concept> provider = new MemoryIndexServiceProvider<Concept>(dataset.getServiceProvider());
        // Create the KB match graph
        WSCKBMatchGraph matchGraph = new WSCKBMatchGraph(dataset.getKb());
        // Create a simple I/O Discovery using a KB Match Graph
        DiscoveryIO<Concept> discovery = new MatchBasedDiscoveryIO<Concept, Boolean>(matchGraph, provider);
        ServiceMatchNetwork<Concept, Boolean> network = new ForwardServiceDiscoverer<Concept, Boolean>(discovery, matchGraph).search(dataset.getRequest());
        return network;
    }

    private void test(WSCTest test) throws IOException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        ServiceMatchNetwork<Concept, Boolean> network = generateGraph(test.dataset());
        // Perform a sanity check, checking the number of services in each layer
        sanityCheck(network, test);
        log.debug("Total test time: {}", stopwatch.stop().toString());
    }

    private void sanityCheck(ServiceMatchNetwork<Concept,Boolean> layers, WSCTest test){
        for(int i=1; i<layers.numberOfLevels()-1; i++){
            assertEquals(test.getExpected()[i-1], layers.getOperationsAtLevel(i).size());
        }
    }

    @Test
    public void testWSC01() throws Exception {
        test(WSCTest.TESTSET_2008_01);
    }

    @Test
    @Ignore
    public void testWSC02() throws Exception {
        test(WSCTest.TESTSET_2008_02);
    }

    @Test
    @Ignore
    public void testWSC03() throws Exception {
        test(WSCTest.TESTSET_2008_03);
    }

    @Test
    @Ignore
    public void testWSC04() throws Exception {
        test(WSCTest.TESTSET_2008_04);
    }

    @Test
    @Ignore
    public void testWSC05() throws Exception {
        test(WSCTest.TESTSET_2008_05);
    }

    @Test
    @Ignore
    public void testWSC06() throws Exception {
        test(WSCTest.TESTSET_2008_06);
    }

    @Test
    @Ignore
    public void testWSC07() throws Exception {
        test(WSCTest.TESTSET_2008_07);
    }

    @Test
    @Ignore
    public void testWSC08() throws Exception {
        test(WSCTest.TESTSET_2008_08);
    }
}