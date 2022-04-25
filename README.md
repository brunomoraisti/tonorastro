# tonorastro app
Aplicativo para rastreamento de encomendas

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
