Continous Integration/Deployment
---
Prosjektet har en gitlab integration pipeline satt opp for å holde god kodestil og at prosjektet ikke feiler uten at vi merker det.  

Innstillingene for pipelinen ligger i [gitlab-ci.yml](../.gitlab-ci.yml) filen.

Om noen ønsker å bidra til eller lære om pipelinen så er det bare å kontakte @heg063.  
Jeg skal prøve å forklare om oppsettet her.

---

Alt som blir gjort i pipelinen kjøres av programmer som er satt opp på en server med adressen 158.39.74.42

**SonarQube** ligger på rota til port 9000: <http://158.39.74.42:9000/>  
SonarQube er en server i seg selv.

**API** fra master ligger på `/docs/javadoc` til html-porten (80):  
<http://158.39.74.42:80/docs/javadoc/>

**HTML**-porten hånderes av en NGINX server og roten <http://158.39.74.42:80/>  
ligger i /srv/nginx/pages/gitlabapi/public/

---

ubuntu@sonarqubedank:~$ whereis nginx  
nginx: /usr/sbin/nginx /etc/nginx /usr/share/nginx  
ubuntu@sonarqubedank:~$ whereis gitlab-runner  
gitlab-runner: /usr/bin/gitlab-runner /etc/gitlab-runner /usr/share/gitlab-runner  
ubuntu@sonarqubedank:~$ whereis mysql  
mysql: /usr/bin/mysql /usr/lib/mysql /etc/mysql /usr/share/mysql /usr/share/man/man1/mysql.1.gz  
ubuntu@sonarqubedank:~$ whereis docker  
docker: /usr/bin/docker /usr/lib/docker /etc/docker /usr/share/man/man1/docker.1.gz  
SonarQube -> sonar.sh  
/opt/sonar/bin/linux-x86-64  

---

/opt/sonar/extensions/plugins/sonar-gitlab-plugin-2.0.1.jar

---

![pipeline](GitLab Pipeline.png)
