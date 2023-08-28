package core.java;

import core.java.entities.Panel;
import core.java.module.log.interfaces.ILog;
import core.java.module.receiver.orientation.interfaces.IOrientationReceiver;
import core.java.tools.Tools;
import io.configuration.Configuration;
import io.configuration.exception.ConfigurationException;
import core.java.dependency.DependencyContainer;
import core.java.module.print.interfaces.IPrint;
import core.java.module.receiver.dimensions.interfaces.IDimensionsReceiver;
import core.java.module.receiver.strict.interfaces.IStrictReceiver;
import core.java.module.receiver.word.interfaces.IWordReceiver;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private Main() {
        //
    }

    public static String[] main(String[] args) throws Exception {
        loadAppConfiguration(args);
        loadGlobalDependencies();

        List<String> soups = new ArrayList<>();

        for (int i = 0; i < Configuration.wordReceiverLength(); i++) {
            loadInstanceDependencies(i);
            soups.add(launch());
        }

        return soups.toArray(new String[0]);
    }

    private static void loadAppConfiguration(String[] args) throws ConfigurationException {
        Arguments.initialize(args);
        Configuration.initialize(Arguments.customDocument());
    }

    private static void loadGlobalDependencies() throws ConfigurationException {
        ILog systemLog = Configuration.getLogInstance();
        IStrictReceiver strictReceiver = Configuration.getStrictReceiverInstance();
        IOrientationReceiver orientationReceiver = Configuration.getOrientationInstance();
        IPrint print = Configuration.getPrinterInstance();

        DependencyContainer.addInstance(ILog.class, systemLog);
        DependencyContainer.addInstance(IStrictReceiver.class, strictReceiver);
        DependencyContainer.addInstance(IOrientationReceiver.class, orientationReceiver);
        DependencyContainer.addInstance(IPrint.class, print);
    }

    private static void loadInstanceDependencies(int index) throws ConfigurationException {
        IWordReceiver[] wordReceivers = Configuration.getWordReceiverInstances();
        IDimensionsReceiver[] dimensionsReceivers = Configuration.getDimensionsReceiverInstances();

        IWordReceiver wordReceiver = Tools.getPosition(wordReceivers, index);
        IDimensionsReceiver dimensionsReceiver = Tools.getPosition(dimensionsReceivers, index);

        DependencyContainer.addInstance(IDimensionsReceiver.class, dimensionsReceiver);
        DependencyContainer.addInstance(IWordReceiver.class, wordReceiver);
    }

    private static String launch() throws Exception {
        IDimensionsReceiver dimensionsReceiver = DependencyContainer.getInstance(IDimensionsReceiver.class);
        IWordReceiver wordsReceiver = DependencyContainer.getInstance(IWordReceiver.class);
        IPrint printer = DependencyContainer.getInstance(IPrint.class);
        IStrictReceiver strictReceiver = DependencyContainer.getInstance(IStrictReceiver.class);

        Panel panel = new Panel(dimensionsReceiver, wordsReceiver, strictReceiver);
        return panel.print(printer);
    }

}