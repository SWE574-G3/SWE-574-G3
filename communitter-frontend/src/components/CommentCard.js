import Card from "react-bootstrap/Card";
import CardTitle from "react-bootstrap/CardTitle";
import React, {useState, useEffect} from "react";
import { useNavigate } from "react-router-dom";
import CardBody from "react-bootstrap/CardBody";

export const CommentCard = ({comment}) => {
    const {author, date: timestamp, content, id} = comment;
    const navigate = useNavigate();

    const directToUserView = () => {
        navigate(`/user/${author.id}`)
    };

    return (
        <Card className="mb-3">
            <CardTitle onClick={directToUserView} style={{cursor: "pointer"}}>
                {author.username} - {" "}
                {new Date(timestamp).toLocaleDateString("tr-TR",{
                    year: "numeric",
                    month: "2-digit",
                    day: "2-digit",
                    hour: "2-digit",
                    minute:"2-digit",
                    hour12: false,
                })}{" "}
            </CardTitle>
            <CardBody>
                <p>{content}</p>
            </CardBody>
        </Card>
    )
}