![alt text](https://github.com/brunomoraisti/tonorastro/blob/master/app/src/main/res/mipmap-mdpi/logo_nome_amarelo.png?raw=true)
### Aplicativo para rastreamento de encomendas

## Link Google Play
https://play.google.com/store/apps/details?id=com.bmorais.tonorastro&hl=pt_BR&gl=US

## Contexto do problema
Em tempos de compras on-line, o rastreamento de encomendas vai ficando cada vez mais difícil de acompanhar, principalmente quando desejamos concentrar todos os rastreamentos em um só local e ainda ser avisado sobre os novos andamentos.

## Solução
Desenvolvimento de um aplicativo para armazenar e acompanhar de forma off-line os andamentos das encomendas. Para utilizar, o usuário deverá informar o código de rastreamento e o app buscará via API os andamentos e armazená-lo no banco de dados local. De acordo com o tempo escolhido é feito uma busca das encomendas que não foram entregues, se tiver algum lançamento novo, é emitido uma notificação informando que teve novo andamento para o pedido específico. Caso não tenha, internet ou a encomenda não tenha sido lançada, ela ficará aguardando o lançamento.

## Dificuldades encontradas
- Em alguns dispositivos o android possui a otimização de bateria o que muitas vezes restringe as atualizações em segundo plano, sendo necessário alterar nas configurações do app.
- No momento o app só está rastreando encomendas postadas nos correios

## Tecnologias utilizadas
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

## API utilizado
Desenvolvi toda a parte back-end utilizando a linguagem PHP com o framework Slim 4 com arquitetura REST e autenticação JWT. Modelo utilizado https://github.com/brunomoraisti/slim4-framework

## Telas
<img src="https://github.com/brunomoraisti/tonorastro/blob/master/img/img3.jpeg?raw=true" alt="J" width="250"/><img src="https://github.com/brunomoraisti/tonorastro/blob/master/img/img4.jpeg?raw=true" alt="J" width="250"/>
#
<img src="https://github.com/brunomoraisti/tonorastro/blob/master/img/img1.jpeg?raw=true" alt="J" width="250"/><img src="https://github.com/brunomoraisti/tonorastro/blob/master/img/img2.jpeg?raw=true" alt="J" width="250"/>


