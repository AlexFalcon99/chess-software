package software.ulpgc.chess.infrastructure.api;


import software.ulpgc.chess.domain.model.File;
import software.ulpgc.chess.domain.model.PieceColor;
import software.ulpgc.chess.domain.model.PieceType;
import software.ulpgc.chess.domain.model.Rank;


public record MakeMoveRequest(
    String roomId,
    PieceType piece,
    //Origen
    File fromFile,
    Rank fromRank,
    //Destino
    File toFile,
    Rank toRank,

    PieceColor playerColor
){}
