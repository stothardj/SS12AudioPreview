print("<?xml version=\"1.0\"?>")
print("\t<venue>")

#levels
print("\t\t<lnum>2</lnum>")
for i in range(0,2):
  print("\t\t<level>")        

  # rows
  print("\t\t\t<rnum>5</rnum>")
  for j in range(1,6):
    print("\t\t\t<row>")

    #seats
    print("\t\t\t\t<snum>10</snum>")
    for k in range(1,11):
      print("\t\t\t\t<seat>") 
      print("\t\t\t\t\t<id>" + str(i) + str(j) + "</id>")
      print("\t\t\t\t\t<x>" + str(k) + "</x>")
      print("\t\t\t\t\t<y>" + str(j) + "</y>")
      print("\t\t\t\t\t<z>" + str(i * 6) + "</z>")
      print("\t\t\t\t</seat>")
    print("\t\t\t</row>")  
  print("\t\t</level>")

print("\t</venue>")
         
