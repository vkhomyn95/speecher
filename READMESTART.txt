Астеріск піднято на amazone https://us-east-2.console.aws.amazon.com/ec2/home?region=us-east-2#Home:

Для підключення чз ssh прібно спершу змінити IP для доступу на самому амазоні
1) Security groups
2) Inbound rules
3) Edit inbound rules
4) Change source from Custom to My ip
5) у root директорії мого компа є ключ ubuntu_asterisk.pem
6) ssh -i "ubuntu_asterisk.pem" ubuntu@ec2-18-223-206-9.us-east-2.compute.amazonaws.com

===============================================
Завантаження та підключення
1) астеріс завантажив 16
2) Сам астеріск розпакований у root
3) audiofork розпакований у  root
4) скопіював "app_audiofork.c" to "asterisk/apps/app_audiofork.c"
5) збілдав астеріск з модулем rm -f ./menuselect.makeopts
6) make menuselect

===============================================
Налаштування астеріску на aws
1) Розташування у директорії etc/asterisk
2) sudo su команда дозволяє зайти з під системи та отримати доступ до встановленого астеріску
3) Audiofork налаштовується у діалплані астеріска /etc/asterisk/extensions/conf
4) Тут треба змінити адресу транслювання звуку на локалхост чз вебсокети

===============================================
Налаштування sip soft phone
1) etc/asterisk/sip.conf
2) Налаштовано 7001 на контекс internal
Дані авторизації
account: 7001@18.223.206.9
Password : 123

3) Налаштовано 7002 на контекс internal
Дані авторизації
account: 7002@18.223.206.9
Password : 456

==========================================
Відвалюється астеріск
1) sudo systemctl restart asterisk
2) module load chan_sip.so