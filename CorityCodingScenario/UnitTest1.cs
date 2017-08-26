
using System.IO;
using SplittingTheBillNS;
using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace SplittingTheBillTests
{
    [TestClass]
    public class UnitTest1
    {
        
        /*
         Test for unacceptable input
         */
        [TestMethod]
        public void TestMethod1()
        {
            SplittingTheBill s = new SplittingTheBill();
            try 
            {
                s.splitTheBill("test1");  
            }
            catch (Exception e)
            {
                Assert.AreEqual(e.Message, "String could not be parsed.", false);
            }
        }

        /*
         Test for unacceptable input
         */
        [TestMethod]
        public void TestMethod2()
        {
            SplittingTheBill s = new SplittingTheBill();
            try 
            {
                s.splitTheBill("test2");   
            }
            catch (Exception e)
            {
                Assert.Fail("Expected no exception, but got: " + e.Message);
            }
        }

        /*
         Test for empty input
         */
        [TestMethod]
        public void TestMethod3()
        {
            SplittingTheBill s = new SplittingTheBill();
            try 
            {
                s.splitTheBill("test3");   
                
                string filePath = Path.GetFullPath(Directory.GetCurrentDirectory() + @"\test3Output.txt");
                using (StreamReader stream = File.OpenText(filePath)) 
                {
                     String line;
                     String[] output = {"($1.99)", "($8.01)", "$10.01"};
                     int idx;
                     for (idx = 0; idx < output.Length; idx++) {
                        line = stream.ReadLine();
                        StringAssert.Contains(line, output[idx]);
                     }
                }
            }
            catch (Exception e)
            {
                Assert.Fail("Expected no exception, but got: " + e.Message);
            }
        }

        /*
         Test for regular case
         */
        [TestMethod]
        public void TestMethod4()
        {
            SplittingTheBill s = new SplittingTheBill();
            try 
            {
                s.splitTheBill("test4");   
                
                string filePath = Path.GetFullPath(Directory.GetCurrentDirectory() + @"\test4Output.txt");
                using (StreamReader stream = File.OpenText(filePath)) 
                {
                     String line;
                     String[] output = {"($1.99)", "($8.01)", "$10.01", "", "$0.98", "($0.97)"};
                     int idx;
                     for (idx = 0; idx < output.Length; idx++) {
                        line = stream.ReadLine();
                        StringAssert.Contains(line, output[idx]);
                     }
                }
            }
            catch (Exception e)
            {
                Assert.Fail("Expected no exception, but got: " + e.Message);
            }
        }

        /*
         Test for nonexistent input
         */
        [TestMethod]
        public void TestMethod5()
        {
            SplittingTheBill s = new SplittingTheBill();
            try 
            {
                s.splitTheBill("test5");   
            }
            catch (Exception e)
            {
                Assert.AreEqual(e.Message, "Error opening the file.", false);
            }
        }
    }
}
