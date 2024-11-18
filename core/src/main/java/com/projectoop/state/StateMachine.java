package com.projectoop.state;

public class StateMachine<T> {
    private T owner;  // Đối tượng sở hữu StateMachine (ví dụ Enemy)
    private IState<T> currentState;  // Trạng thái hiện tại

    public StateMachine(T owner) {
        this.owner = owner;
    }

    public void changeState(IState<T> newState) {
        if (currentState != null) {
            currentState.onExit(owner);  // Thoát trạng thái hiện tại
        }
        currentState = newState;
        if (currentState != null) {
            currentState.onEnter(owner);  // Vào trạng thái mới
        }
    }

    public void update() {
        if (currentState != null) {
            currentState.onExecute(owner);  // Thực thi logic của trạng thái
        }
    }

    public IState<T> getCurrentState() {
        return currentState;
    }
}
