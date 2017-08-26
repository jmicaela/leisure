using System;
using System.IO;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using SplittingTheBillNS;

namespace SplittingTheBillNS
{
    
    public struct campers
    {
        public float[] receipts;
    }

    public class SplittingTheBill
    {
    
        static void Main(string[] args)
        {
            SplittingTheBill s = new SplittingTheBill();
            s.splitTheBill(null);            
        }

        public void splitTheBill(String file)
        {
            List<campers> trip = new List<campers>();
            int currentCampTrip = -1;           // indexing for trip list
            int camp = 0;
            int currentCamperIdx = -1;          // index representing each camp
            int camperReceipts = 0;             // line that indicate number of receipts
            int currentCamperReceiptsIdx = 0;   // index representing each receipt
            float currentReceipt = 0;           // stores sum of receipts of camper 
            float tempFloat;

            if (String.IsNullOrEmpty(file)) 
            {
                Console.WriteLine("Enter input file name");
                file = Console.ReadLine();
            }
                
                
            string filePath = Path.GetFullPath(Directory.GetCurrentDirectory() + @"\" + file + ".txt");
            try 
            {
                using (StreamReader stream = File.OpenText(filePath)) 
                {
                    String line;
                    while ((line = stream.ReadLine()) != null)
                    {
                        // if parsing number of participants
                        if ((camp == 0) && (line != "0")) { 
                            if (Int32.TryParse(line, out camp))
                            {
                                currentCampTrip += 1;
                                campers c = new campers();
                                c.receipts = new float[camp];
                                trip.Add(c);
                                currentCamperIdx = -1;
                            }
                            else
                            {
                                Console.WriteLine("String could not be parsed.");
                            }
                        }
                        // if parsing a receipt charge
                        else if (currentCamperIdx < camp && currentCamperReceiptsIdx < camperReceipts)
                        {
                            if (float.TryParse(line, out tempFloat))
                            {
                                currentReceipt += tempFloat;
                                currentCamperReceiptsIdx += 1;
                                if (currentCamperReceiptsIdx == camperReceipts) // if done summing receipts
                                {
                                    trip[currentCampTrip].receipts[currentCamperIdx] = currentReceipt;                                    
                                    currentReceipt = 0;
                                    camperReceipts = 0;
                                    currentCamperReceiptsIdx = 0;
                                    if (currentCamperIdx == camp-1) 
                                    {
                                        currentCamperIdx = 0;
                                        camp = 0;
                                    }
                                }
                            }   
                            else
                            {
                                Console.WriteLine("String could not be parsed.");
                            }
                        }
                        // if parsing number of receipts
                        else if (camp > 0 && camperReceipts == 0)
                        {
                            if (Int32.TryParse(line, out camperReceipts))
                            { 
                                currentCamperReceiptsIdx = 0;
                                currentCamperIdx += 1; // increment camp index after parsing all receipt for a camp

                            } 
                            else 
                            { 
                                Console.WriteLine("String could not be parsed.");
                            }
                        }
                        // if line == "0"
                        else 
                        {
                            break;
                        }

                    }
                }
            }
            catch (Exception e)
            {
                Console.WriteLine("Error opening the file.", e.ToString());
            }

            // Process division of receipts for each camp trip
            if (trip.Count > 0) {
                camp = 0;
                currentCamperIdx = 0;
                float receiptPool = 0;

                string outputPath = System.IO.Path.GetFullPath(Directory.GetCurrentDirectory() + @"\" + file + "Output.txt");
                System.IO.StreamWriter output = new System.IO.StreamWriter(outputPath);

                for (camp = 0; camp < trip.Count; camp++) {
                    receiptPool = trip[camp].receipts.Sum();
                    camperReceipts = trip[camp].receipts.Count();
                        
                    for (currentCamperIdx = 0; currentCamperIdx < camperReceipts; currentCamperIdx++) {
                        float balance = (receiptPool/camperReceipts) - trip[camp].receipts[currentCamperIdx];
                        if (balance < 0) 
                        {
                            output.WriteLine("($" + Math.Round(Math.Abs(balance), 2) + ")");
                        }
                        else
                        {
                            output.WriteLine("$" + Math.Round(balance, 2));
                        }
                    }
                        
                    output.WriteLine("");
                }
                output.Close();
            }
            return;
        } 
    }
}
