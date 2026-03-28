import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class BikeRental {
    private boolean isRegisteredUser;
    private String emailAddress;
    private String location;
    private LocalDateTime tripStartTime;
    private String bikeID;
    private boolean locationValid;
 UserRegistration userRegistration=new UserRegistration();
 ActiveRental activeRental;
 List<ActiveRental> activeRentalList=new LinkedList<>();
 public void  simulateApplicationInput(){
     System.out.println("This is the simulation of the e-bike rental process");
     Scanner sc =new Scanner(System.in);
     System.out.println("State if the user is a registers user (true/false)");
     isRegisteredUser=sc.nextBoolean();
     sc.nextLine();
     System.out.println("Enter the email address of the user:");
     emailAddress=sc.nextLine();
     System.out.println("Enter the location of the bike:");
     location=sc.nextLine();
     System.out.println();
     System.out.println("Simulating the analysis of the rental request...");
     bikeID=analyseRequest(isRegisteredUser,emailAddress ,location);
     if(!locationValid){
         return;
     }
     System.out.println("Simulating bike reservation…");
     reserveBike(bikeID);
     System.out.println("Displaying the active rentals…");
     viewActiveRentals();
     System.out.println("Simulating trip end…");
     removeTrip(bikeID);
     System.out.println("Displaying the active rentals after trip end…");
     viewActiveRentals();
 }
 private String analyseRequest(boolean isRegisteredUser,String emailAddress,String location){
   if(isRegisteredUser){
       System.out.println("Welcome back,"+emailAddress+"!");
   }else{
       System.out.println("You’re not our registered user. Please consider registering.");

   }
     bikeID= validateLocation(location);


   return bikeID;
 }
 private  String validateLocation(String location){
     for(Bike bike:BikeDatabase.bikes){
          if(bike.getLocation().equals(location)){
              if(bike.isAvailable()){
                  System.out.println("A bike is available at the location you requested.");
                  System.out.println();
                  locationValid=true;
                  return bike.getBikeID();
              }
          }
     }
     System.out.println("Sorry, no bikes are available at the location you requested. ");
     System.out.println("Exiting the program.Please try again later when we have bikes available at your location.Thank you!");
    locationValid=false;
     return null;

 }
 private void  reserveBike(String bikeID){
     if(bikeID==null||bikeID.isEmpty()){
         System.out.println("Sorry, we’re unable to reserve a bike at this time. Please try again later.");
     }else{
         for(Bike bike:BikeDatabase.bikes){
             if(bike.getBikeID().equals(bikeID)){
                 tripStartTime=LocalDateTime.now();
              bike.setAvailable(false);
              bike.setLastUsedTime(tripStartTime);
                 System.out.println("Reserving bike with ID:"+bikeID);
                 System.out.println("Please follow the on-screen direction to locate your bike and start your trip.");
                 System.out.println();
                 activeRental=new ActiveRental(bikeID,emailAddress,tripStartTime);
                 activeRentalList.add(activeRental);
                 break;
             }
         }
     }
 }
 private void viewActiveRentals(){
     if(activeRentalList.isEmpty()){
         System.out.println("No active rentals at the moment. ");
     }else{
         for(ActiveRental rental:activeRentalList){
             System.out.println(rental);
         }
     }
 }
 private void removeTrip(String bikeID){
     Iterator<ActiveRental>it=activeRentalList.iterator();
     while(it.hasNext()){
         ActiveRental a=it.next();
         if(a.getBikeID().equals(bikeID)){
             it.remove();;
             break;
         }
     }
     for(Bike b:BikeDatabase.bikes){
        if(b.getBikeID().equals(bikeID)) {
            b.setAvailable(true);
            b.setLastUsedTime(LocalDateTime.now());
            System.out.println("Your trip has ended. Thank you for riding with us!");
            System.out.println();
            break;
        }
     }
 }

}
