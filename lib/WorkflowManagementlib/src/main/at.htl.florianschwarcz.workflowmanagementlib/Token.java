package at.htl.florianschwarcz.workflowmanagementlib;

import java.util.HashSet;

public class Token {
    private HashSet<Element> state;
    private String result;

    public Token(Start start) {
        state = new HashSet<>();
        state.add(start);
    }

    public HashSet<Element> getState() {
        return state;
    }
    public String getResult(){
        return result;
    }
    public void setResult(String result){
        this.result = result;
    }

    public boolean proceed(Element state){
        this.state.remove(state);
        if(state instanceof End){
            this.state.remove(state);
        }else if(state instanceof SingleNextElement){
            if(((SingleNextElement)state).getNext() == null){
                return false;
            }
            this.state.add(((SingleNextElement)state).getNext());
        }else if(state instanceof Fork){
            for(Element element : ((MultipleNextElement)state).getNext()){
                if(element == null){
                    return false;
                }
            }
            this.state.addAll(((MultipleNextElement)state).getNext());
        }else if(state instanceof Branch){
            if(((Branch)state).getNext(result) == null){
                return false;
            }
            this.state.add(((Branch)state).getNext(result));
        }
        return true;
    }
}
