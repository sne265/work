import java.util.Stack;

public class CellCaretaker {
 
    private Memento obj;
    Stack<Memento> history = new Stack<>();
    public void save(CellDataModel cellData){
        this.obj=(Memento) cellData.save();
        history.push(obj);
    }
 
    public void undo(CellDataModel cellData){
    	cellData.undoToLastSave(history.pop());
    }
}
