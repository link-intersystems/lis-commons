The lis-common-event library provides support for Java event handling.

#EventMethod

An EventMethod is an adapter that can adapt method references,
such as Runnable, Consumer and BiConsumer, to event listener methods.

    public class EventMethodUsageExample {
    
     public static void main(String[] args) {
         EventMethodUsageExample eventMethodUsageExample = new EventMethodUsageExample();
         eventMethodUsageExample.setSelection(3);
         eventMethodUsageExample.setSelection(5);
     }
    
     private ListSelectionListener l1 = ListSelectionEventMethod.VALUE_CHANGED.listener(this::printEventFired);
    
     private ListSelectionListener l2 = ListSelectionEventMethod.VALUE_CHANGED.listener(
             this::printSelectionChanged,
             ListSelectionEvent::getLastIndex,
             ">>> ", e -> !e.getValueIsAdjusting());
    
     private ListSelectionModel selectionModel = new DefaultListSelectionModel();
    
     EventMethodUsageExample() {
         selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
         selectionModel.addListSelectionListener(l1);
         selectionModel.addListSelectionListener(l2);
     }
    
     public void setSelection(int index) {
         selectionModel.setValueIsAdjusting(true);
         selectionModel.setSelectionInterval(index, index);
         selectionModel.setValueIsAdjusting(false);
     }
    
     private void printEventFired(ListSelectionEvent e) {
         int firstIndex = e.getFirstIndex();
         int lastIndex = e.getLastIndex();
         boolean isAdjusting = e.getValueIsAdjusting();
         System.out.println("Selection model event fired:" +
                 " firstIndex= " + firstIndex +
                 " lastIndex= " + lastIndex +
                 " isAdjusting= " + isAdjusting);
     }
    
     private void printSelectionChanged(int selectedIndex, String indent) {
         System.out.println(indent + "Index " + selectedIndex + " selected");
     }
    }


 Running the example code above will output:

 
     Selection model event fired: firstIndex= 3 lastIndex= 3 isAdjusting= true
     >>> Index 3 selected
     Selection model event fired: firstIndex= 3 lastIndex= 3 isAdjusting= false
     Selection model event fired: firstIndex= 3 lastIndex= 5 isAdjusting= true
     >>> Index 5 selected
     Selection model event fired: firstIndex= 3 lastIndex= 5 isAdjusting= false
