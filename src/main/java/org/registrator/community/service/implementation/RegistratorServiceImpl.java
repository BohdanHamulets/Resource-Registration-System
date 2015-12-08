package org.registrator.community.service.implementation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xwpf.usermodel.Borders;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.registrator.community.dao.daofactory.DaoFactory;
import org.registrator.community.dao.utils.HibernateUtil;
import org.registrator.community.dto.DiscreteParameterDTO;
import org.registrator.community.dto.LinearParameterDTO;
import org.registrator.community.dto.PointAreaDTO;
import org.registrator.community.dto.PoligonAreaDTO;
import org.registrator.community.dto.ResourceAreaDTO;
import org.registrator.community.dto.ResourceDTO;
import org.registrator.community.dto.ResourceDiscreteValueDTO;
import org.registrator.community.dto.ResourceLinearValueDTO;
import org.registrator.community.dto.ResourceTypeDTO;
import org.registrator.community.dto.SegmentLinearDTO;
import org.registrator.community.entity.Area;
import org.registrator.community.entity.DiscreteParameter;
import org.registrator.community.entity.LinearParameter;
import org.registrator.community.entity.Resource;
import org.registrator.community.entity.ResourceType;
import org.registrator.community.entity.ResourceDiscreteValue;
import org.registrator.community.entity.ResourceLinearValue;
import org.registrator.community.entity.Tome;
import org.registrator.community.entity.User;
import org.registrator.community.service.interfaces.RegistratorService;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class RegistratorServiceImpl implements RegistratorService {

	private XWPFDocument document;
	private FileOutputStream out;
	private XWPFParagraph paragraph;
	private Document documentPDF;
	private FileOutputStream outPDF;
	private PdfWriter writerPDF;
	
	public RegistratorServiceImpl() {

		document = new XWPFDocument();
		paragraph = document.createParagraph();

	}

	public void saveAllResourcesToPDF() throws DocumentException, FileNotFoundException{
		documentPDF = new Document();
		outPDF = new FileOutputStream("AllResources.pdf");
		writerPDF=PdfWriter.getInstance(documentPDF, outPDF);
		documentPDF.open();
		Paragraph paragraph=new Paragraph();
		paragraph.add(showAllResources().toString());
		documentPDF.add(paragraph);
		documentPDF.close();
	}
	
	public void saveAllResourcesToWord() throws IOException {
		paragraph.setBorderBottom(Borders.BASIC_BLACK_DASHES);
		paragraph.setBorderLeft(Borders.BASIC_BLACK_DASHES);
		paragraph.setBorderRight(Borders.BASIC_BLACK_DASHES);
		paragraph.setBorderTop(Borders.BASIC_BLACK_DASHES);

		out = new FileOutputStream(new File("AllResources.docx"));
		XWPFRun run = paragraph.createRun();
		
		run.setText(showAllResources().toString());

		document.write(out);
		out.close();
		System.out.println("Data sucsessfully saved to 'AllResources.docx' document");
	}

	
	public void saveAllResourceTypesToPDF() throws FileNotFoundException, DocumentException{
		documentPDF = new Document();
		outPDF = new FileOutputStream("AllResourceTypes.pdf");
		writerPDF=PdfWriter.getInstance(documentPDF, outPDF);
		documentPDF.open();
		Paragraph paragraph=new Paragraph();
		paragraph.add(showAllResources().toString());
		documentPDF.add(paragraph);
		documentPDF.close();
		
	}
	
	public void saveAllResourceTypesToWord() throws IOException{
		paragraph.setBorderBottom(Borders.BASIC_BLACK_DASHES);
		paragraph.setBorderLeft(Borders.BASIC_BLACK_DASHES);
		paragraph.setBorderRight(Borders.BASIC_BLACK_DASHES);
		paragraph.setBorderTop(Borders.BASIC_BLACK_DASHES);

		out = new FileOutputStream(new File("AllTypeOfResources.docx"));
		XWPFRun run = paragraph.createRun();
		
		run.setText(showAllTypeOfResources().toString());

		document.write(out);
		out.close();
		System.out.println("Data sucsessfully saved to 'AllTypeOfResources.docx' document");
	}
	
	
	/**
     * Method addResourseType provides the record
     * of new resource type in database including 
     * linear and discrete parameters
     */
	@Override
	public void addResourseType(ResourceTypeDTO resourceTypeDTO) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		// creating the resource entity to save in database
		ResourceType resourceEntity = new ResourceType();
		resourceEntity.setTypeName(resourceTypeDTO.getTypeName());
		DaoFactory.get().getResourceTypeDao().add(resourceEntity);

		List<LinearParameterDTO> linearParameterDTOs = resourceTypeDTO.getLinearParameters();
		List<DiscreteParameterDTO> discreteParametersDTOs = resourceTypeDTO.getDiscreteParameters();
		
		for (LinearParameterDTO linearParameterDTO : linearParameterDTOs) {
			LinearParameter lineSizeEntyty = new LinearParameter();
			lineSizeEntyty.setResourceType(resourceEntity);
			lineSizeEntyty.setDescription(linearParameterDTO.getDescription());
			lineSizeEntyty.setUnitName(linearParameterDTO.getUnitName());
			DaoFactory.get().getLineSizeDao().add(lineSizeEntyty);
		}
		 		
		for (DiscreteParameterDTO discreteParameterDTO : discreteParametersDTOs) {
			DiscreteParameter discreteValueEntity = new DiscreteParameter();
			discreteValueEntity.setResourceType(resourceEntity);
			discreteValueEntity.setDescription(discreteParameterDTO.getDescription());
			discreteValueEntity.setUnitName(discreteParameterDTO.getUnitName());
			DaoFactory.get().getDiscreteValueDao().add(discreteValueEntity);
		}
		session.close();
	}

	/**
     * Method addResource provides the record
     * of new resource (of existing in database type)
     * with concrete values/значення of area 
     * coordinates and line/discrete parameters
     */
	@Override
	public void addResource(ResourceDTO resourceDTO) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		// list of PoligonsAreaDTO for table area
		List<PoligonAreaDTO> poligonAreaDTOs = resourceDTO.getResourceArea().getPoligons();

		// list of ResourceLinearDTO for table store of line sizes
		List<ResourceLinearValueDTO> resourceLinearDTOs = resourceDTO.getResourceLinear();

		// list of ResourceDiscreteDTO for table store of discrete values
		List<ResourceDiscreteValueDTO> resourceDiscreteDTOs = resourceDTO.getResourceDiscrete();

		// filling table list_of_resources
		ResourceType resourceType = (ResourceType) session.createCriteria(ResourceType.class)
				.add(Restrictions.eq("typeName", resourceDTO.getResourceType().getTypeName())).uniqueResult();
		User registrator = (User) session.createCriteria(User.class)
				.add(Restrictions.eq("firstName", resourceDTO.getRegistratorName())).uniqueResult();
		Tome tome = (Tome) session.createCriteria(Tome.class)
				.add(Restrictions.eq("identifier", resourceDTO.getTomeIdentifier())).uniqueResult();

		Resource resourceEntity = new Resource(resourceType, resourceDTO.getIdentifier(), resourceDTO.getDescription(),
				registrator, resourceDTO.getDate(), resourceDTO.getStatus().toString(), tome,
				resourceDTO.getReasonInclusion());
		DaoFactory.get().getResourceDao().add(resourceEntity);

		// filling table area
		for ( PoligonAreaDTO poligonAreaDTO :poligonAreaDTOs) {			
			List<PointAreaDTO> pointAreaDTOs = poligonAreaDTO.getPoints();
			for (PointAreaDTO point : pointAreaDTOs) {
				Area area = new Area();
				area.setResource(resourceEntity);
				area.setOrderNumber(point.getOrderNumber());
				area.setLatitude(point.getDecimalLatitude());
				area.setLongitude(point.getDecimalLongitude());
				DaoFactory.get().getAreaDao().add(area);
			}
		}
		
		// filling table store of line sizes	
		for (ResourceLinearValueDTO resourceLinearDTO : resourceLinearDTOs) {
			LinearParameterDTO linearParameterDTO = resourceLinearDTO.getLinearParameterDTO();

			Criteria cr = session.createCriteria(LinearParameter.class);
			Criterion resource = Restrictions.eq("resourceType", resourceType);
			Criterion name = Restrictions.eq("description", linearParameterDTO.getDescription());
			LogicalExpression andExp = Restrictions.and(resource, name);
			cr.add(andExp);
			LinearParameter linearParameter = (LinearParameter) cr.uniqueResult();
			List<SegmentLinearDTO> segments = resourceLinearDTO.getSegments();

			for (SegmentLinearDTO segment : segments) {
				ResourceLinearValue storeOfLineSizes = new ResourceLinearValue();
				storeOfLineSizes.setResource(resourceEntity);
				storeOfLineSizes.setLinearParameter(linearParameter);
				storeOfLineSizes.setMinValue(segment.getBegin());
				storeOfLineSizes.setMaxValue(segment.getEnd());
				DaoFactory.get().getStoreOfLineSizesDao().add(storeOfLineSizes);
			}
		}

		// filling table store of discrete values
		for (ResourceDiscreteValueDTO resourceDiscreteDTO : resourceDiscreteDTOs) {
			DiscreteParameterDTO discreteParameterDTO = resourceDiscreteDTO.getDiscreteParameterDTO();

			Criteria cr = session.createCriteria(DiscreteParameter.class);
			Criterion resource = Restrictions.eq("resourceType", resourceType);
			Criterion name = Restrictions.eq("description", discreteParameterDTO.getDescription());
			LogicalExpression andExp = Restrictions.and(resource, name);
			cr.add(andExp);
			DiscreteParameter discreteParameter = (DiscreteParameter) cr.uniqueResult();

			List<Double> values = resourceDiscreteDTO.getValues();
			for (Double value : values) {
				ResourceDiscreteValue storeOfDiscreteValues = new ResourceDiscreteValue();
				storeOfDiscreteValues.setResource(resourceEntity);
				storeOfDiscreteValues.setDiscreteParameter(discreteParameter);
				storeOfDiscreteValues.setValue(value);
				DaoFactory.get().getStoreOfDiscreteValuesDao().add(storeOfDiscreteValues);
			}
		}

		session.close();
	}

	public Resource addResourceNoTransaction(ResourceDTO resourceDTO) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		// list for table area
				List<PoligonAreaDTO> poligonAreaDTOs = resourceDTO.getResourceArea().getPoligons();

				// list for table store of line sizes
				List<ResourceLinearValueDTO> resourceLinearDTOs = resourceDTO.getResourceLinear();

				// list for table store of discrete values
				List<ResourceDiscreteValueDTO> resourceDiscreteDTOs = resourceDTO.getResourceDiscrete();

				// filling table list_of_resources
				ResourceType resourceType = (ResourceType) session.createCriteria(ResourceType.class)
						.add(Restrictions.eq("typeName", resourceDTO.getResourceType().getTypeName())).uniqueResult();

				User registrator = (User) session.createCriteria(User.class)
						.add(Restrictions.eq("firstName", resourceDTO.getRegistratorName())).uniqueResult();

				Tome tome = (Tome) session.createCriteria(Tome.class)
						.add(Restrictions.eq("identifier", resourceDTO.getTomeIdentifier())).uniqueResult();

				Resource resourceEntity = new Resource(resourceType, resourceDTO.getIdentifier(), resourceDTO.getDescription(),
						registrator, resourceDTO.getDate(), resourceDTO.getStatus().toString(), tome,
						resourceDTO.getReasonInclusion());

				DaoFactory.get().getResourceDao().add(resourceEntity);

				// filling table area
				for (int i = 0; i < poligonAreaDTOs.size(); i++) {
					PoligonAreaDTO poligonAreaDTO = poligonAreaDTOs.get(i);
					List<PointAreaDTO> pointAreaDTOs = poligonAreaDTO.getPoints();
					for (int j = 0; j < pointAreaDTOs.size(); j++) {
						Area area = new Area();
						PointAreaDTO point = pointAreaDTOs.get(j);
						Double latitude = point.getLatitudeDegrees() + point.getLatitudeMinutes() / 60d
								+ point.getLatitudeSeconds() / 3600d;
						Double longitude = point.getLongitudeDegrees() + point.getLongitudeMinutes() / 60d
								+ point.getLongitudeSeconds() / 3600d;
						area.setResource(resourceEntity);
						area.setOrderNumber(point.getOrderNumber());
						area.setLatitude(latitude);
						area.setLongitude(longitude);
						DaoFactory.get().getAreaDao().add(area);
					}
				}

				// filling table store of line sizes
				for (int i = 0; i < resourceLinearDTOs.size(); i++) {
					ResourceLinearValueDTO resourceLinearDTO = resourceLinearDTOs.get(i);
					LinearParameterDTO linearParameterDTO = resourceLinearDTO.getLinearParameterDTO();

					Criteria cr = session.createCriteria(LinearParameter.class);
					Criterion resource = Restrictions.eq("resourceType", resourceType);
					Criterion name = Restrictions.eq("description", linearParameterDTO.getDescription());
					LogicalExpression andExp = Restrictions.and(resource, name);
					cr.add(andExp);
					LinearParameter linearParameter = (LinearParameter) cr.uniqueResult();
					List<SegmentLinearDTO> segments = resourceLinearDTO.getSegments();

					for (int j = 0; j < segments.size(); j++) {
						ResourceLinearValue storeOfLineSizes = new ResourceLinearValue();
						SegmentLinearDTO segment = segments.get(j);
						storeOfLineSizes.setResource(resourceEntity);
						storeOfLineSizes.setLinearParameter(linearParameter);
						storeOfLineSizes.setMinValue(segment.getBegin());
						storeOfLineSizes.setMaxValue(segment.getEnd());
						DaoFactory.get().getStoreOfLineSizesDao().add(storeOfLineSizes);
					}
				}

				// filling table store of discrete values
				for (int i = 0; i < resourceDiscreteDTOs.size(); i++) {
					ResourceDiscreteValueDTO resourceDiscreteDTO = resourceDiscreteDTOs.get(i);
					DiscreteParameterDTO discreteParameterDTO = resourceDiscreteDTO.getDiscreteParameterDTO();

					Criteria cr = session.createCriteria(DiscreteParameter.class);
					Criterion resource = Restrictions.eq("resourceType", resourceType);
					Criterion name = Restrictions.eq("description", discreteParameterDTO.getDescription());
					LogicalExpression andExp = Restrictions.and(resource, name);
					cr.add(andExp);
					DiscreteParameter discreteParameter = (DiscreteParameter) cr.uniqueResult();

					List<Double> values = resourceDiscreteDTO.getValues();
					for (int j = 0; j < values.size(); j++) {
						ResourceDiscreteValue storeOfDiscreteValues = new ResourceDiscreteValue();
						storeOfDiscreteValues.setResource(resourceEntity);
						storeOfDiscreteValues.setDiscreteParameter(discreteParameter);
						storeOfDiscreteValues.setValue(values.get(j));
						DaoFactory.get().getStoreOfDiscreteValuesDao().add(storeOfDiscreteValues);
			}
		}
		return resourceEntity;
	}

	//
	@Override
	public List<ResourceTypeDTO> showAllTypeOfResources() {
		Session session = HibernateUtil.getSessionFactory().openSession();

		List<ResourceTypeDTO> resourceTypeDTO = new ArrayList<ResourceTypeDTO>();

		List<ResourceType> resourceType = DaoFactory.get().getResourceTypeDao().getAll();

		List<LinearParameter> lineSizeList = DaoFactory.get().getLineSizeDao().getAll();
		List<LinearParameterDTO> linearParameterDTOList = new ArrayList<LinearParameterDTO>();
		List<DiscreteParameterDTO> discreteParameterDTOList = new ArrayList<DiscreteParameterDTO>();
		List<DiscreteParameter> discreteValueList = DaoFactory.get().getDiscreteValueDao().getAll();

		// Add linear parameters to DTO class
		for (LinearParameter ls : lineSizeList) {
			LinearParameterDTO lpDTO = new LinearParameterDTO();
			lpDTO.setDescription(ls.getDescription());
			lpDTO.setUnitName(ls.getUnitName());
			linearParameterDTOList.add(lpDTO);
		}

		// Add discrete parameters to DTO class
		for (DiscreteParameter dv : discreteValueList) {
			DiscreteParameterDTO dpDTO = new DiscreteParameterDTO();
			dpDTO.setDescription(dv.getDescription());
			dpDTO.setUnitName(dv.getUnitName());
			discreteParameterDTOList.add(dpDTO);
		}
		// Add ResourceType to DTO class

		// List<ResourceType> resourceType =
		// DaoFactory.get().getResourceTypeDao().getAll();

		for (ResourceType rt : resourceType) {
			// List<LineSize> lineSizeList =
			// DaoFactory.get().getLineSizeDao().getAll();

			List<LinearParameterDTO> linearParameterDTOList1 = new ArrayList<LinearParameterDTO>();
			List<DiscreteParameterDTO> discreteParameterDTOList1 = new ArrayList<DiscreteParameterDTO>();

			// Add linear parameters to DTO class
			for (LinearParameter ls : lineSizeList) {
				LinearParameterDTO lpDTO = new LinearParameterDTO();
				lpDTO.setDescription(ls.getDescription());
				lpDTO.setUnitName(ls.getUnitName());
				linearParameterDTOList.add(lpDTO);
			}

			// Add discrete parameters to DTO class
			for (DiscreteParameter dv : discreteValueList) {
				DiscreteParameterDTO dpDTO = new DiscreteParameterDTO();
				dpDTO.setDescription(dv.getDescription());
				dpDTO.setUnitName(dv.getUnitName());
				discreteParameterDTOList.add(dpDTO);
			}

			// Add ResourceType to DTO class
			ResourceTypeDTO rtDTO = new ResourceTypeDTO();
			rtDTO.setDiscreteParameters(discreteParameterDTOList);
			rtDTO.setLinearParameters(linearParameterDTOList);
			rtDTO.setTypeName(rt.getTypeName());
			resourceTypeDTO.add(rtDTO);
		}
		return resourceTypeDTO;
	}

	//
	@Override
	public ResourceDTO showResourceByIdentifier(String identifier) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		// getting resource from database with identifier
		Resource resource = DaoFactory.get().getResourceDao().findByIdentifier(identifier);

		ResourceAreaDTO areaDTO = new ResourceAreaDTO();
		List<ResourceLinearValueDTO> resLinDTOs = new ArrayList<ResourceLinearValueDTO>();
		List<ResourceDiscreteValueDTO> resDiscDTOs = new ArrayList<ResourceDiscreteValueDTO>();

		List<LinearParameter> lineSizeList = (List<LinearParameter>) session.createCriteria(LinearParameter.class)
				.add(Restrictions.eq("resourceType", resource.getType())).list();

		for (LinearParameter lineSize : lineSizeList) {
			Criteria cr = session.createCriteria(ResourceLinearValue.class);
			Criterion res = Restrictions.eq("resource", resource);
			Criterion linesizeParam = Restrictions.eq("lineSize", lineSize);
			LogicalExpression andExp = Restrictions.and(res, linesizeParam);
			cr.add(andExp);
			List<ResourceLinearValue> resourcelineParameters = (List<ResourceLinearValue>) cr.list();
			List<SegmentLinearDTO> segments = new ArrayList<SegmentLinearDTO>();
			for (ResourceLinearValue resourcelineParameter : resourcelineParameters) {
				SegmentLinearDTO segment = new SegmentLinearDTO();
				segment.setBegin(resourcelineParameter.getMinValue());
				segment.setEnd(resourcelineParameter.getMaxValue());
				segments.add(segment);
			}
			ResourceLinearValueDTO resLinDTO = new ResourceLinearValueDTO();
			resLinDTO.setLinearParameterDTO(new LinearParameterDTO(lineSize.getDescription(), lineSize.getUnitName()));
			resLinDTO.setSegments(segments);
			resLinDTOs.add(resLinDTO);
		}

		List<Area> areas = (List<Area>) session.createCriteria(Area.class).add(Restrictions.eq("resource", resource))
				.list();
		List<PoligonAreaDTO> poligons = new ArrayList<PoligonAreaDTO>();
		for (Area area : areas) {
			// if (area.getNumberOfPoint() == 1) {
			PoligonAreaDTO poligon = new PoligonAreaDTO();
			List<PointAreaDTO> pointDTOs = new ArrayList<PointAreaDTO>();
			PointAreaDTO pointDTO = new PointAreaDTO();
			pointDTO.setOrderNumber(area.getOrderNumber());
			pointDTO.setLatitudeValues(area.getLatitude());
			pointDTO.setLongitudeValues(area.getLongitude());
			pointDTOs.add(pointDTO);
			poligon.setPoints(pointDTOs);
			poligons.add(poligon);
			// }

		}
		areaDTO.setPoligons(poligons);

		List<DiscreteParameter> discreteValueList = (List<DiscreteParameter>) session.createCriteria(DiscreteParameter.class)
				.add(Restrictions.eq("resourceType", resource.getType())).list();

		for (DiscreteParameter discreteValue : discreteValueList) {
			Criteria cr = session.createCriteria(ResourceDiscreteValue.class);
			Criterion res = Restrictions.eq("resource", resource);
			Criterion discValueParam = Restrictions.eq("discreteValue", discreteValue);
			LogicalExpression andExp = Restrictions.and(res, discValueParam);
			cr.add(andExp);
			List<ResourceDiscreteValue> resourceDiscreteParameters = (List<ResourceDiscreteValue>) cr.list();
			List<Double> values = new ArrayList<Double>();
			for (ResourceDiscreteValue resourceDiscreteParameter : resourceDiscreteParameters) {
				values.add(resourceDiscreteParameter.getValue());
			}
			ResourceDiscreteValueDTO resDiscDTO = new ResourceDiscreteValueDTO();
			resDiscDTO.setDiscreteParameterDTO(
					new DiscreteParameterDTO(discreteValue.getDescription(), discreteValue.getUnitName()));
			resDiscDTO.setValues(values);
			resDiscDTOs.add(resDiscDTO);
		}

		ResourceTypeDTO resTypeDTO = new ResourceTypeDTO();
		resTypeDTO.setTypeName(resource.getType().getTypeName());
		String registratorName = resource.getRegistrator().getLastName() + resource.getRegistrator().getFirstName()
				+ resource.getRegistrator().getMiddleName();

		// adding all information in ResourceDTO object
		ResourceDTO resourceDTO = new ResourceDTO();
		resourceDTO.setResourceType(resTypeDTO);
		resourceDTO.setIdentifier(resource.getIdentifier());
		resourceDTO.setDescription(resource.getDesctiption());
		resourceDTO.setDate(resource.getDate());
		resourceDTO.setRegistratorName(registratorName);
		resourceDTO.setResourceArea(areaDTO);
		resourceDTO.setResourceDiscrete(resDiscDTOs);
		resourceDTO.setResourceLinear(resLinDTOs);
		resourceDTO.setTomeIdentifier(resource.getTome().getIdentifier());
		resourceDTO.setReasonInclusion(resource.getReasonInclusion());
		resourceDTO.setStatus(resource.getStatus());

		session.close();
		return resourceDTO;

	}

	// the implementation of this method is not finished yet. Anyone of us can
	// make changes here
	@Override
	public List<ResourceDTO> showAllResources() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();

		List<ResourceDTO> resourceDTO = new ArrayList<ResourceDTO>();

		List<Resource> resource = DaoFactory.get().getResourceDao().getAll();

		for (Resource rs : resource) {

			ResourceTypeDTO rtDTO = new ResourceTypeDTO();
			rtDTO.setTypeName(rs.getType().getTypeName());

			ResourceDTO rDTO = new ResourceDTO();
			rDTO.setDate(rs.getDate());
			rDTO.setDescription(rs.getDesctiption());
			rDTO.setIdentifier(rs.getIdentifier());
			rDTO.setReasonInclusion(rs.getReasonInclusion());
			rDTO.setRegistratorName(rs.getRegistrator().getFirstName() + "   " + rs.getRegistrator().getLastName());
			rDTO.setTomeIdentifier(rs.getTome().getIdentifier());
			rDTO.setResourceType(rtDTO);
			rDTO.setStatus(rs.getStatus());
			resourceDTO.add(rDTO);
		}

		transaction.commit();
		session.close();

		return resourceDTO;
	}

}
