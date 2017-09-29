'''
Make sure you have >requests< installed -> pip install requests
This project uses the WTFPL License
'''

import urllib.request, urllib.parse, os, sys
from urllib.request import Request, urlopen
try:
    import requests
except:
    print('Please install the library "requests"')
    sys.exit(0)


user_agent = 'Mozilla/5.0 (Windows NT 6.1; WOW64; rv:33.0) Gecko/20120101 Firefox/33.0'
headers = {'User-Agent' : user_agent}
opener = urllib.request.build_opener()
opener.addheaders = [('User-Agent', user_agent)]


#Generate folders if nonexistent
directories = ["Uebungen/AlgorithmenUndDatenstrukturen", "Uebungen/DiskreteMathematik",
               "Uebungen/EProg", "Uebungen/LineareAlgebra"]

for i in directories:
    if not os.path.isdir(i):
        os.makedirs(i)
        print("Folder generated: ", i) 

sys.exit(0)


#START Algorithmen und Datenstrukturen
print('[1/4] Algorithmen und Datenstrukturen:')
ad_ex_counter = 0
ad_so_counter = 0
ad_ex_url = 'https://www.cadmo.ethz.ch/education/lectures/HS17/DA/uebungen/Blatt'+str(ad_ex_counter)+'.pdf'
ad_so_url = 'https://www.cadmo.ethz.ch/education/lectures/HS17/DA/uebungen/Loesung'+str(ad_so_counter)+'.pdf'

try:
    while opener.open(ad_ex_url).getcode() == 200:
        print(ad_ex_url)
        urllib.request.urlretrieve(ad_ex_url, 'Uebungen/AlgorithmenUndDatenstrukturen/Blatt'+str(ad_ex_counter)+'.pdf')
        ad_ex_counter += 1
        ad_ex_url = 'https://www.cadmo.ethz.ch/education/lectures/HS17/DA/uebungen/Blatt'+str(ad_ex_counter)+'.pdf'
except urllib.error.HTTPError:
    pass
print('')
try:
    while opener.open(ad_so_url).getcode() == 200:
        print(ad_so_url)
        urllib.request.urlretrieve(ad_so_url, 'Uebungen/AlgorithmenUndDatenstrukturen/Loesung'+str(ad_so_counter)+'.pdf')
        ad_so_counter += 1
        ad_so_url = 'https://www.cadmo.ethz.ch/education/lectures/HS17/DA/uebungen/Loesung'+str(ad_so_counter)+'.pdf'
except urllib.error.HTTPError:
    pass

print('[1/4]\n\n')


#START Diskrete Mathematik //no solutions available yet //This will break at 08,09,010
print('[2/4] Diskrete Mathematik:')
dm_ex_counter = 1
dm_so_counter = 1
dm_ex_url = 'http://www.crypto.ethz.ch/teaching/lectures/DM17/u0'+str(dm_ex_counter)+'.pdf'
dm_so_url = ''

try:
    while opener.open(dm_ex_url).getcode() == 200:
        print(dm_ex_url)
        urllib.request.urlretrieve(dm_ex_url, 'Uebungen/DiskreteMathematik/u0'+str(dm_ex_counter)+'.pdf')
        dm_ex_counter += 1
        dm_ex_url = 'http://www.crypto.ethz.ch/teaching/lectures/DM17/u0'+str(dm_ex_counter)+'.pdf'
except urllib.error.HTTPError:
    pass

print('[2/4]\n\n')


#START Einführung in die Programmierung
print('[3/4] Einführung in die Programmierung:')
ep_ex_counter = 0
ep_mat_counter = 0
ep_url = 'https://www.ethz.ch/content/dam/ethz/special-interest/infk/inst-cs/lst-dam/documents/Education/Classes/Fall2017/0027_Intro/Homework/u'
ep_ex_url = ep_url+str(ep_ex_counter)+'.pdf'
ep_mat_url = ep_url+str(ep_mat_counter)+'.zip'

try:
    while opener.open(ep_ex_url).getcode() == 200:
        print(ep_ex_url)
        r = requests.get(ep_ex_url, allow_redirects=False, headers=headers)
        with open('Uebungen/EProg/u'+str(ep_ex_counter)+'.pdf', 'wb') as f:
            for chunk in r.iter_content(1024):
                f.write(chunk)
        ep_ex_counter += 1
        ep_ex_url = ep_url+str(ep_ex_counter)+'.pdf'
except urllib.error.HTTPError:
    pass
print('')
try:
    while opener.open(ep_mat_url).getcode() == 200:
        print(ep_mat_url)
        r = requests.get(ep_mat_url, allow_redirects=False, headers=headers)
        with open('Uebungen/EProg/u'+str(ep_mat_counter)+'.zip', 'wb') as f:
            for chunk in r.iter_content(1024):
                f.write(chunk)
        ep_mat_counter += 1
        ep_mat_url = ep_url+str(ep_mat_counter)+'.pdf'
except urllib.error.HTTPError:
    pass

print('[3/4]\n\n')


#START Lineare Algebra
print('[4/4] Lineare Algebra:')
la_ex_counter = 0
la_ex_url = ''

print('[4/4]\n\n')


input('EOF') #Just so Windows users don't get butthurt about not seeing the output
