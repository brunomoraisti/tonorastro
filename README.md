![alt text](https://github.com/brunomoraisti/tonorastro/blob/master/app/src/main/res/mipmap-mdpi/logo_nome_amarelo.png?raw=true)
### Aplicativo para rastreamento de encomendas

## Link Google Play
https://play.google.com/store/apps/details?id=com.bmorais.tonorastro&hl=pt_BR&gl=US

## Como funciona?
O usuário informa o código de rastreamento, o app busca via API os andamentos da encomenda e armazena no banco de dados local. De acordo com o tempo escolhido o app busca de forma automática se possui novos andamentos, se tiver emiti uma notificação informando que teve novo andamento

## Tecnologias
- Android Nativo (Java)
- Banco de dados local (SQLite)
- Retrofit (Http)
- Worke (Execução em segundo plano)
- Lottie (Animação de imagens)
- Firebase

## Estrutura
    tonorastro
    ├── adapter/
    ├── dao/
    ├── firebase/
    ├── http/
    ├── lib/
    ├── model/
    ├── pages/
    ├── service/
    ├── src/
    ├── AndamentoObjetoActivity
    ├── PrincipalActivity
    └── SplashActivity

## API
Foi desenvolvido uma API utilizando a linguagem PHP com o framework Slim 4 com arquitetura REST e autenticação JWT. Modelo utilizado https://github.com/brunomoraisti/slim4-framework

## Telas
<img src="https://github.com/brunomoraisti/tonorastro/blob/master/img/img3.jpeg?raw=true" alt="J" width="250"/><img src="https://github.com/brunomoraisti/tonorastro/blob/master/img/img4.jpeg?raw=true" alt="J" width="250"/>
#
<img src="https://github.com/brunomoraisti/tonorastro/blob/master/img/img1.jpeg?raw=true" alt="J" width="250"/><img src="https://github.com/brunomoraisti/tonorastro/blob/master/img/img2.jpeg?raw=true" alt="J" width="250"/>


