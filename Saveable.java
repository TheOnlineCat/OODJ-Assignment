public abstract interface Saveable {

    void Save();

    void Delete();

    boolean Load(String ID);
}
