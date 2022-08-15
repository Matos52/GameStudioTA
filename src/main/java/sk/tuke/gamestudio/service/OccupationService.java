package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Occupation;

import java.util.List;

public interface OccupationService {

    public void addOccupation(Occupation occupation);

    public List<Occupation > getOccupations();
}
