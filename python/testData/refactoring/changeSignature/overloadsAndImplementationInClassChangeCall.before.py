from typing import overload


class A:
    @overload
    def foo(self, value: str) -> None:
        pass

    @overload
    def foo(self, value: int) -> str:
        pass

    def foo(self, value):
        return None


A().foo<caret>("")