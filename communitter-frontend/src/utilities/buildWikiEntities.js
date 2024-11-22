export const buildWikiEntities=(entitiesObj)=>{
    
    let entities=[];
    for(let entity in entitiesObj){
        entities.push(entitiesObj[entity])
    };
    
    let sanitizedEntities=entities.map((entity)=>{
        
        let sanitizedEntity={
            wikiEntity:{
                code:"",
                label:"",
                url:"",
                description:"",
                parentCodes:[]              
            }
        };
        let wikiEntity=sanitizedEntity.wikiEntity;
        
        wikiEntity.code=entity.id;
        wikiEntity.label=entity?.labels?.en?.value;
        wikiEntity.description=entity?.descriptions?.en?.value;
        wikiEntity.url=`https://wikidata.org/wiki/${entity.id}`;
        
        entity?.claims?.["P279"]?.forEach((parent) => {
            wikiEntity.parentCodes.push(parent.mainsnak.datavalue.value.id);           
        });
        
        return sanitizedEntity;
    

    }
)
    
    return sanitizedEntities;
}