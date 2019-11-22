package bokarev;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Classes {
    public static class TestPackage {
        final Integer packageId;
        final String jsScript, functionName;
        final ArrayList<OneTest> testsLists;

        @JsonCreator
        TestPackage(
                @JsonProperty("packageId") Integer packageId,
                @JsonProperty("jsScript") String jsScript,
                @JsonProperty("functionName") String functionName,
                @JsonProperty("tests") ArrayList<OneTest> testsLists) {
            this.packageId = packageId;
            this.jsScript = jsScript;
            this.functionName = functionName;
            this.testsLists = testsLists;
        }

        TestPackage(TestForImpl test) {
            this.packageId = test.packageId;
            this.jsScript = test.jsScript;
            this.functionName = test.functionName;
            this.testsLists = new ArrayList<>();
            this.testsLists.add(test.oneTest);
        }

        public Integer getPackageId() {
            return packageId;
        }

        public String getJsScript() {
            return jsScript;
        }

        public String getFunctionName() {
            return functionName;
        }

        public ArrayList<OneTest> getTestsLists() {
            return testsLists;
        }
    }

    static class OneTest {
        String testName;
        Double expectedResult;
        Object[] params;
        Boolean result;

        @JsonCreator
        OneTest(@JsonProperty("testName") String testName,
                @JsonProperty("expectedResult") Double expectedResult,
                @JsonProperty("params") Object[] params,
                @JsonProperty("result") Boolean result) {
            this.testName = testName;
            this.expectedResult = expectedResult;
            this.params = params;
            this.result = null;
        }

        public String getTestName() {
            return testName;
        }

        public Double getExpectedResult() {
            return expectedResult;
        }

        public Object[] getParams() {
            return params;
        }

        public Boolean getResult() {
            return result;
        }
    }

    public static class TestForImpl {
        final Integer packageId;
        final String jsScript, functionName;
        OneTest oneTest;

        TestForImpl(TestPackage test, int indexOfTest) {
            this.packageId = test.packageId;
            this.jsScript = test.jsScript;
            this.functionName = test.functionName;

            this.oneTest = new OneTest(
                    test.testsLists.get(indexOfTest).testName,
                    test.testsLists.get(indexOfTest).expectedResult,
                    test.testsLists.get(indexOfTest).params,
                    null);
        }

        void setResult (Boolean result){
            this.oneTest.result = result;
        }

        public Integer getPackageId() {
            return packageId;
        }

        public String getJsScript() {
            return jsScript;
        }

        public String getFunctionName() {
            return functionName;
        }

        public OneTest getOneTest() {
            return oneTest;
        }
    }

    public static final class TestGetter {
        int packageId;

        TestGetter(int packageId) {
            this.packageId = packageId;
        }

        public int getPackageId() {
            return packageId;
        }
    }
}
