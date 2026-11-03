export class BoardView {
    constructor(containerElement, onMoveSelectedCallback) {
        this.container = containerElement;
        this.onMoveSelected = onMoveSelectedCallback;
        this.selectedSquare = null;
        this.initialSetup = [
            ['♜','♞','♝','♛','♚','♝','♞','♜'],
            ['♟','♟','♟','♟','♟','♟','♟','♟'],
            ['','','','','','','',''],
            ['','','','','','','',''],
            ['','','','','','','',''],
            ['','','','','','','',''],
            ['♙','♙','♙','♙','♙','♙','♙','♙'],
            ['♖','♘','♗','♕','♔','♗','♘','♖']
        ];
        this.boardMatrix = JSON.parse(JSON.stringify(this.initialSetup));
    }

    reset() {
        this.boardMatrix = JSON.parse(JSON.stringify(this.initialSetup));
        this.selectedSquare = null;
        this.render();
    }

    applyMove(fromSquare, toSquare) {
        const fromCol = fromSquare.charCodeAt(0) - 97;
        const fromRow = 8 - parseInt(fromSquare[1], 10);
        const toCol = toSquare.charCodeAt(0) - 97;
        const toRow = 8 - parseInt(toSquare[1], 10);

        this.boardMatrix[toRow][toCol] = this.boardMatrix[fromRow][fromCol];
        this.boardMatrix[fromRow][fromCol] = '';
        this.selectedSquare = null;
        this.render();
    }

    render() {
        this.container.innerHTML = '';
        for (let row = 0; row < 8; row++) {
            for (let col = 0; col < 8; col++) {
                const square = document.createElement('div');
                const isLight = (row + col) % 2 === 0;
                const squareName = String.fromCharCode(97 + col) + (8 - row);

                square.className = `square ${isLight ? 'light' : 'dark'}`;
                square.dataset.square = squareName;
                square.innerText = this.boardMatrix[row][col];

                if (this.selectedSquare === squareName) {
                    square.classList.add('selected');
                }

                square.onclick = () => this._handleSquareClick(squareName, row, col);
                this.container.appendChild(square);
            }
        }
    }

    _handleSquareClick(squareName, row, col) {
        if (!this.selectedSquare) {
            if (this.boardMatrix[row][col] !== '') {
                this.selectedSquare = squareName;
                this.render();
            }
        } else {
            if (this.selectedSquare === squareName) {
                this.selectedSquare = null;
                this.render();
                return;
            }
            const origin = this.selectedSquare;
            this.selectedSquare = null;
            this.render();
            this.onMoveSelected(origin, squareName);
        }
    }
}