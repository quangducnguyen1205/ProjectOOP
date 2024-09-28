package com.projectoop.state;

public interface IState<T> {
    void onEnter(T t);  // Xử lý khi state được kích hoạt
    void onExecute(T t);  // Xử lý logic khi state đang được thực thi
    void onExit(T t);  // Xử lý khi thoát khỏi state
}
