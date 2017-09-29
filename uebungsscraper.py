'''
Make sure you have `requests` and `bs4` installed -> pip install requests/bs4
This project uses the WTFPL License

Checklist | Uebung | Loesung | Material
----------|-----------------------------
AlgDat    |    y   |    y    |   n/a
----------|-----------------------------
DisMat    |    y   |    y    |   n/a
----------|-----------------------------
EProg     |    y   |    n    |    y
----------|-----------------------------
LinAlg    |    y   |    y    |    y

'''

import urllib.request, urllib.parse, os, sys, http.client
from urllib.request import Request, urlopen
from html.parser import *
try:
    import requests
except:
    print('Installing BeautifulSoup is NOT optional, m8')
    sys.exit(0)
try:
    from bs4 import BeautifulSoup, SoupStrainer
except:
    print('Installing BeautifulSoup is NOT optional, m8')
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
        print("This folder was generated: ",i) 
    else:
        print("This folder already exists: ",i)
print("\n\n")


#START Algorithmen und Datenstrukturen
print('[1/4] Algorithmen und Datenstrukturen:')
ad_link = 'https://www.cadmo.ethz.ch/education/lectures/HS17/DA/index.html'
ad_links =[]
request = Request(ad_link, headers={'User-Agent' : user_agent})
soup = BeautifulSoup(urllib.request.urlopen(request), 'html.parser')

soup = str(soup).partition('<table id="uebungen">')[-1]
soup = soup.rpartition('<h3>Fragen und Kommentare</h3>')[0]
soup = BeautifulSoup(soup, 'html.parser')

ad_link = 'https://www.cadmo.ethz.ch/education/lectures/HS17/DA/'

for link in soup.find_all('a'):
    ad_links.append(link.get('href'))

for link in ad_links:
    split = urllib.parse.urlsplit(link)
    filename = 'Uebungen/AlgorithmenUndDatenstrukturen/'+split.path.split('/')[-1]
    link = 'https://www.cadmo.ethz.ch/education/lectures/HS17/DA/'+link
    print(link)
    urllib.request.urlretrieve(link, filename)

print('[1/4]\n\n')


#START Diskrete Mathematik
print('[2/4] Diskrete Mathematik:')
dm_link = 'http://www.crypto.ethz.ch/teaching/lectures/DM17/'
dm_links =[]
request = Request(dm_link, headers={'User-Agent' : user_agent})
soup = BeautifulSoup(urllib.request.urlopen(request), 'html.parser')

soup = str(soup).partition('<h3>Übungsblätter</h3>')[-1]
soup = soup.rpartition('<h3>Übungsgruppen</h3>')[0]
soup = BeautifulSoup(soup, 'html.parser')

for link in soup.find_all('a'):
    dm_links.append(link.get('href'))

for link in dm_links:
    split = urllib.parse.urlsplit(link)
    filename = 'Uebungen/DiskreteMathematik/'+split.path.split('/')[-1]
    link = 'http://www.crypto.ethz.ch/teaching/lectures/DM17/'+link
    print(link)
    urllib.request.urlretrieve(link, filename)

print('[2/4]\n\n')


#START Einführung in die Programmierung
print('[3/4] Einführung in die Programmierung:')
ep_link = 'http://www.lst.inf.ethz.ch/education/einfuehrung-in-die-programmierung-i--252-0027-.html'
ep_links =[]
request = Request(ep_link, headers={'User-Agent' : user_agent})
soup = BeautifulSoup(urllib.request.urlopen(request), 'html.parser')

soup = str(soup).partition('<div class="par basecomponent parsys contains-table">')[-1]
soup = soup.rpartition('<a class="accordionAnchor" href="#"><span class="title">Literatur und Hilfsmittel</span></a>')[0]
soup = BeautifulSoup(soup, 'html.parser')

for link in soup.find_all('a'):
    ep_links.append(link.get('href'))

del ep_links[0]

for link in ep_links:
    split = urllib.parse.urlsplit(link)
    filename = 'Uebungen/EProg/'+split.path.split('/')[-1]
    print(link)
    r = requests.get(link, allow_redirects=False, headers=headers)
    with open(filename, 'wb') as f:
        for chunk in r.iter_content(1024):
            f.write(chunk)
    
print('[3/4]\n\n')


#START Lineare Algebra
print('[4/4] Lineare Algebra:')
la_link = 'http://igl.ethz.ch/teaching/linear-algebra/la2017/'
la_links =[]
user_agent = 'Mozilla/5.0 (Windows NT 6.1; WOW64; rv:33.0) Gecko/20120101 Firefox/33.0'
request = Request(la_link, headers={'User-Agent' : user_agent})
soup = BeautifulSoup(urllib.request.urlopen(request), 'html.parser')

soup = str(soup).partition('<h3>Übungen</h3>')[-1]
soup = soup.rpartition('href="https://echo.ethz.ch/s/"  target="_blank" title="M')[0]
soup = BeautifulSoup(soup, 'html.parser')

for link in soup.find_all('a'):
    la_links.append(link.get('href'))

for link in la_links:
    split = urllib.parse.urlsplit(link)
    filename = 'Uebungen/LineareAlgebra/'+split.path.split('/')[-1]
    link = 'http://igl.ethz.ch/teaching/linear-algebra/la2017/'+link
    print(link)
    urllib.request.urlretrieve(link, filename)

print('[4/4]\n\n')


input('EOF') #Just so Windows users don't get butthurt about not seeing the output
