package algorithms;

import structure.Modification;

public interface RecuitInterface {

    int getSize();

    Modification modificationFunction();

    Double improvementFunction(Modification modification);

    void commitFunction(Modification modification);

}
