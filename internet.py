# Importing flask module in the project is mandatory
# An object of Flask class is our WSGI application.
import random
from flask import Flask , request , render_template , redirect , url_for , jsonify

# Flask constructor takes the name of 
# current module (__name__) as argument.
app = Flask(__name__)

# The route() function of the Flask class is a decorator, 
# which tells the application which URL should call 
# the associated function.

# ‘/’ URL is bound with hello_world() function.

def is_float(input_string):
    try:
        float(input_string)  # Try to convert the string to a float
        return True  # Conversion succeeded, so it's a float
    except ValueError:
        return False  


def log_data(user1,user2,dist):
    print('out')
    # Python code to create a file
    file = open('C:\\Users\\sirha\\OneDrive\Desktop\\Coding\dev_projects\\app_dev\\Collision_Detection_Log','a')
    print('outtt')
    
    text = user1 + ' is the user 1' + user2 + 'is the user2' + 'collision dist is' + str(dist) +'\n'
    file.write(text)
    file.close()


     
big_data = {}
user = []
lat = []
lon = []
thresh = 0


userer = 'rajabhagavat'

bu1 = 1.4
bu2 = 1.3

@app.route('/', methods=['POST','GET'])
def post_data():
    
    baduser = []

    if request.method == 'POST':
        data = request.get_json()  # Assumes JSON data is sent in the request body
        #print(data.get('userid'))
        #print(data.get('latitude'))
        #print(data.get('longitude'))

        
        big_data[data.get('userid')] = [data.get('latitude'),data.get('longitude')]
        
        lat =[]
        lon = []
        user = []

        for value in big_data.values():
            print(value)
            if(is_float(value[0]) and is_float(value[1])):
                print('dummy test here',float(value[0]))
                lat.append(float(value[0]))

                lon.append(float(value[1]))

        for key in big_data:
             user.append(key) 
        print('users going to print',user)  

       
        if len(lat)==2:
             dist = float(lat[0])-float(lat[1])
             dist2 = float(lon[0])-float(lon[1])
             dist3 = (dist**2 + dist2**2)**0.5
             print('cal samllest dist', dist3)
             if(abs(dist3)<0.00001):
                log_data(user[0],user[1],dist3)
             #if abs(float(lat[0])-float(lat[1]))<0.003 :
                

    
        
             global p_lat , p_long
             p_lat= big_data[key][0]
             p_long = big_data[key][1]

       
                       

        return render_template('home.html')
    
'''
    if request.method == 'GET':
        print("getted ")
        if len(baduser)==0:
            return {"bu1" : "1.67","bu2":"2.67"}
        else:
             return {"bu1":baduser[0],"bu2":baduser[1]}
    '''
                
 
    #return render_template("home.html")



@app.route('/get_location', methods=['GET'])
def get_location():
    # Simulate getting latitude and longitude data
    latitude = p_lat  # Replace with actual data source
    print('latitude is ',latitude)
    longitude = p_long  # Replace with actual data source
    print(p_lat,p_long)
    return render_template("index.html",lat=latitude,long=longitude)
    

# main driver function
if __name__ == '__main__':

	# run() method of Flask class runs the application 
	# on the local development server.
	app.run()
