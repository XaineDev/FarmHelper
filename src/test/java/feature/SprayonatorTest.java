package feature;

import lombok.Getter;
import org.jetbrains.annotations.TestOnly;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SprayonatorTest {

    private static final Random random = new Random();

    public static final Pattern plotNumberRegex = Pattern.compile("Plot\\s-\\s(\\d+)");
    public static final Pattern plotSprayedItemTimeRegex = Pattern.compile("Sprayed with ([a-zA-z]+ ?[a-zA-z]+) (\\d+)[ms] ?(\\d+)?[ms]?");

    @TestOnly
    public static boolean plotNumberRegexTest() {
        return true;
    }

    @TestOnly
    @Test
    public void plotSprayedItemTest() {
        System.out.println("FORMAT");
        System.out.printf("TESTCASE `%s` FAILED {REASON}: %s - %s%n%n", "{TESTCASE}", "{EXPECTED RESULT}", "{ACTUAL RESULT}");

        // flag for all tests passed
        boolean passedTests = true;

        /*
            this will run a test on each case defined and will check to see if it matches successfully
         */
        final StringTest[] tests = new StringTest[]{
                // empty string should fail
                new StringTest("", false),
                // no item should fail
                new StringTest("Sprayed with 30m 10s", false),
                // no time in string should fail
                new StringTest("Sprayed with Tasty Cheese aaa aa", false),
                // should pass
                new StringTest("Sprayed with Tasty Cheese 30m 30s", true),
                new StringTest("Sprayed with Tasty Cheese 30m 0s", true),
                new StringTest("Sprayed with Tasty Cheese 3m 30s", true),
                new StringTest("Sprayed with Tasty Cheese 3m 3s", true),
                new StringTest("Sprayed with Tasty Cheese 3s", true),

                new StringTest("Sprayed with Compost 30m 30s", true),
                new StringTest("Sprayed with Compost 30m 0s", true),
                new StringTest("Sprayed with Compost 3m 30s", true),
                new StringTest("Sprayed with Compost 3m 3s", true),
                new StringTest("Sprayed with Compost 3s", true),
        };


        System.out.println("TESTING PARSING");
        boolean stringTestPassed = true;
        for (StringTest test : tests) {
            boolean result = plotSprayedItemTimeRegex.matcher(test.testCase).find();
            if (!test.match(result)) {
                stringTestPassed = false;
                passedTests = false;
            }
        }
        /*
            this will run a test on each case defines and will check the regex results with the expected results
         */
        final StringResultTest[] resultTests = new StringResultTest[]{
                // testing different items at the same time
                new StringResultTest("Sprayed with Tasty Cheese 30m 0s", "Tasty Cheese", "30", "0"),
                new StringResultTest("Sprayed with Compost 30m 0s", "Compost", "30", "0"),
                new StringResultTest("Sprayed with Plant Matter 30m 0s", "Plant Matter", "30", "0"),
                new StringResultTest("Sprayed with Honey Jar 30m 0s", "Honey Jar", "30", "0"),
                new StringResultTest("Sprayed with Dung 30m 0s", "Dung", "30", "0"),

                // testing different times with same item
                new StringResultTest("Sprayed with Tasty Cheese 30m 30s", "Tasty Cheese", "30", "30"),
                new StringResultTest("Sprayed with Tasty Cheese 30m 3s", "Tasty Cheese", "30", "3"),
                new StringResultTest("Sprayed with Tasty Cheese 3m 30s", "Tasty Cheese", "3", "30"),
                new StringResultTest("Sprayed with Tasty Cheese 30s", "Tasty Cheese", "30", null),
                new StringResultTest("Sprayed with Tasty Cheese 3s", "Tasty Cheese", "3", null),

                new StringResultTest("Sprayed with Compost 30m 30s", "Compost", "30", "30"),
                new StringResultTest("Sprayed with Compost 30m 3s", "Compost", "30", "3"),
                new StringResultTest("Sprayed with Compost 3m 30s", "Compost", "3", "30"),
                new StringResultTest("Sprayed with Compost 30s", "Compost", "30", null),
                new StringResultTest("Sprayed with Compost 3s", "Compost", "3", null),
        };


        System.out.println("TESTING MATCH RESULTS");
        for (StringResultTest resultTest : resultTests) {
            final Matcher resultMatch = plotSprayedItemTimeRegex.matcher(resultTest.testCase);
            boolean result = resultMatch.find();
            // if it failed to parse we can continue
            if (!result) {
                passedTests = false;
                System.out.printf("TESTCASE `%s` FAILED PARSE%n", resultTest.testCase);
                continue;
            }

            // get all matched groups from the result
            String[] matcherResults = new String[resultMatch.groupCount()];

            for (int i = 1; i < resultMatch.groupCount(); i++) {
                String matchedString;
                try {
                    matchedString = resultMatch.group(i);
                } catch (IndexOutOfBoundsException e) {
                    continue;
                }
                if (matchedString == null) {
                    continue;
                }
                matcherResults[i - 1] = matchedString;
            }

            // test :D
            if (!resultTest.match(matcherResults)) {
                System.out.printf("TESTCASE `%s` FAILED MATCH: %s | %s%n", resultTest.testCase, Arrays.toString(resultTest.expectedResults), Arrays.toString(matcherResults));
                passedTests = false;
            }
        }


        if (passedTests) {
            System.out.println("PASSED ALL TESTS");
        } else if (stringTestPassed) {
            System.out.printf("PASSED ALL STRINGTESTS%n");
        } else {
            System.out.printf("FAILED ALL TESTS%n");
        }

    }

    private int generateNumber(int lowerBound, int upperBound) {
        return random.nextInt(upperBound-lowerBound) + lowerBound;
    }

    @Getter
    private static class StringTest {
        private final String testCase;
        private final boolean expectedResult;

        StringTest(String testCase, boolean expectedResult) {
            this.testCase = testCase;
            this.expectedResult = expectedResult;
        }

        /**
         * @param result the result from the testcase
         * @return true if result is expected
         */
        protected boolean match(boolean result) {
            if (result != expectedResult) {
                System.out.printf("TESTCASE `%s` FAILED MATCH: %s - %s%n", testCase, expectedResult, result);
                return false;
            }
            return true;
        }
    }

    @Getter
    private static class StringResultTest {
        private final String testCase;
        private final String[] expectedResults;

        StringResultTest(String testCase, String... expectedResults) {
            this.testCase = testCase;
            this.expectedResults = expectedResults;
        }

        /**
         * @param result the results to match the expected testcase results to
         * @return true if all test cases match
         */
        protected boolean match(String... result) {
            // if they don't have same results it cant match
            if (result.length != expectedResults.length) return false;
            // if both have no length then match
            if (result.length == 0) return true;

            boolean matched = true;

            int resultCount = 0;
            for (String expectedResult : expectedResults) {
                if (!Objects.equals(expectedResult, expectedResults[resultCount])) {
                    System.out.printf("TESTCASE `%s` FAILED MATCH: %s - %s%n", testCase, expectedResult, expectedResults[resultCount]);
                    matched = false; // if they don't match then set return flag to false
                }
                resultCount++;
            }
            return matched;
        }
    }
}
