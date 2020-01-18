public interface Observable
{
public void addObserver(CellValue ob);
public void removeObserver(CellValue ob);
public void notifyObservers();
}
