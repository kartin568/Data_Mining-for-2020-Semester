start /wait C:\LDRA_Toolsuite\contestbed 7ab16f70-7406-4e03-81aa-bdf8df07613a /create_set=system  /1q 
start /wait C:\LDRA_Toolsuite\contestbed 7ab16f70-7406-4e03-81aa-bdf8df07613a /add_set_file="D:/Project/577cff4d-d67d-42f3-ba8a-3cc9fab37e5e/1.0/testDemo/MD5.cpp" /1q 
start /wait C:\LDRA_Toolsuite\contestbed 7ab16f70-7406-4e03-81aa-bdf8df07613a /add_set_file="D:/Project/577cff4d-d67d-42f3-ba8a-3cc9fab37e5e/1.0/testDemo/Source.cpp" /1q 
start /wait C:\LDRA_Toolsuite\contestbed 7ab16f70-7406-4e03-81aa-bdf8df07613a /add_set_file="D:/Project/577cff4d-d67d-42f3-ba8a-3cc9fab37e5e/1.0/testDemo/hush.cpp" /1q 
start /wait C:\LDRA_Toolsuite\contestbed 7ab16f70-7406-4e03-81aa-bdf8df07613a /112a35q  /generate_code_review=HTML /generate_quality_review=HTML /noauto_macro /cquality_model=MISRA-C:2012  /cppquality_model=GJB_8114
