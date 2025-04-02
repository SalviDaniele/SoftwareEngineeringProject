package Listeners;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * This class provides methods to change the status of the target via side effects.
 *
 * @param <T> the type of the target
 */
public class GuiStatusModifier<T>{

    /**
     * The target associated with this GuiStatusModifier.
     */
    private T target;

    /**
     * An ExecutorService that manages a pool of cached threads.
     */
    protected ExecutorService threads = Executors.newCachedThreadPool();

    /**
     * Getter method for the target
     *
     * @return the current target
     */
    public T getTarget(){
        return this.target;
    }

    /**
     * Setter method for the target.
     *
     * @param target the target to be added
     */
    public void setTarget(T target){
        this.target =target;
    }

    /**
     * Modify the status of the target by performing the given action
     * if the target and the action are not null.
     *
     * @param action the action to be performed on the target
     */
    public void modifyStatus(Consumer<T> action){
        if(this.target !=null)
            action.accept(this.target);
    }
}
